package crushedtaro.deeplinebackend.domain.approval.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.approval.dto.*;
import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.approval.entity.ApprovalLine;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalType;
import crushedtaro.deeplinebackend.domain.approval.repository.ApprovalRepository;
import crushedtaro.deeplinebackend.domain.attendance.repository.AttendanceRepository;
import crushedtaro.deeplinebackend.domain.attendance.service.AttendanceService;
import crushedtaro.deeplinebackend.domain.audit.annotation.AuditLog;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.notification.service.NotificationProducer;
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;
import crushedtaro.deeplinebackend.global.util.SecurityUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalService {

  private final SecurityUtil securityUtil;
  private final ApprovalRepository approvalRepository;
  private final MemberRepository memberRepository;
  private final AttendanceRepository attendanceRepository;
  private final AttendanceService attendanceService;
  private final NotificationProducer notificationProducer;

  @Transactional
  public void registerApproval(ApprovalRegistDTO approvalRegistDTO) {
    log.info("[ApprovalService] registerApproval Start");

    String memberId = securityUtil.getCurrentMemberId();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    ApprovalType type = ApprovalType.GENERAL;
    if (approvalRegistDTO.type() != null) {
      try {
        type = ApprovalType.valueOf(approvalRegistDTO.type());
      } catch (IllegalArgumentException e) {
        throw new CustomException(ErrorCode.INVALID_APPROVAL_TYPE);
      }
    }

    Approval approval =
        Approval.builder()
            .title(approvalRegistDTO.title())
            .content(approvalRegistDTO.content())
            .member(member)
            .status(ApprovalStatus.PENDING)
            .approvalType(type)
            .startDate(approvalRegistDTO.startDate())
            .endDate(approvalRegistDTO.endDate())
            .build();

    List<Integer> approverCodes = approvalRegistDTO.approverCodes();

    if (approverCodes == null || approverCodes.isEmpty()) {
      throw new CustomException(ErrorCode.APPROVER_REQUIRED);
    }

    for (int i = 0; i < approverCodes.size(); i++) {

      int approverCode = approverCodes.get(i);

      Member approver =
          memberRepository
              .findById(approverCode)
              .orElseThrow(() -> new CustomException(ErrorCode.APPROVER_NOT_FOUND));

      if (approver.getMemberCode() == member.getMemberCode()) {
        throw new CustomException(ErrorCode.CANNOT_APPROVE_SELF);
      }

      ApprovalStatus lineStatus = (i == 0) ? ApprovalStatus.PENDING : ApprovalStatus.WAITING;

      ApprovalLine approvalLine =
          ApprovalLine.builder()
              .approval(approval)
              .approver(approver)
              .status(lineStatus)
              .lineOrder(i + 1)
              .build();

      approval.getApprovalLines().add(approvalLine);
    }

    Approval savedApproval = approvalRepository.save(approval);

    if (!approvalRegistDTO.approverCodes().isEmpty()) {
      Integer firstApproverCode = approvalRegistDTO.approverCodes().get(0);

      String approverId = memberRepository.findById(firstApproverCode).get().getMemberId();

      notificationProducer.sendNotification(
          approverId,
          "새로운 결재 문서가 도착했습니다: " + savedApproval.getTitle(),
          "/approvals/" + savedApproval.getApprovalCode());
    }

    log.info("[ApprovalService] registerApproval End");
  }

  @Transactional(readOnly = true)
  public List<ApprovalListDTO> getSentApprovals() {

    log.info("[ApprovalService] getSentApprovals Start");
    String memberId = securityUtil.getCurrentMemberId();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    return approvalRepository.findAllByMemberOrderByCreatedAtDesc(member).stream()
        .map(
            a ->
                new ApprovalListDTO(
                    a.getApprovalCode(),
                    a.getTitle(),
                    a.getMember().getMemberName(),
                    a.getStatus(),
                    a.getCreatedAt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ApprovalListDTO> getReceivedApprovals() {
    log.info("[ApprovalService] getReceivedApprovals Start");
    String memberId = securityUtil.getCurrentMemberId();

    Member member = securityUtil.findMemberById(memberId);

    return approvalRepository.findWaitApprovals(member.getMemberCode()).stream()
        .map(
            a ->
                new ApprovalListDTO(
                    a.getApprovalCode(),
                    a.getTitle(),
                    a.getMember().getMemberName(),
                    a.getStatus(),
                    a.getCreatedAt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ApprovalDetailDTO getApprovalDetail(Long approvalCode) {
    log.info("[ApprovalService] getApprovalDetail Start");

    Approval approval =
        approvalRepository
            .findById(approvalCode)
            .orElseThrow(() -> new CustomException(ErrorCode.APPROVAL_NOT_FOUND));

    List<ApprovalLineDTO> approvalLineDTOS =
        approval.getApprovalLines().stream()
            .map(
                line ->
                    new ApprovalLineDTO(
                        line.getLineOrder(),
                        line.getApprover().getMemberName(),
                        (line.getApprover().getPosition() != null)
                            ? line.getApprover().getPosition().getPositionName()
                            : "",
                        line.getStatus(),
                        line.getComment(),
                        line.getProcessedAt()))
            .toList();

    String deptName =
        (approval.getMember().getDepartment() != null)
            ? approval.getMember().getDepartment().getDeptName()
            : "";

    return new ApprovalDetailDTO(
        approval.getApprovalCode(),
        approval.getTitle(),
        approval.getContent(),
        approval.getMember().getMemberName(),
        deptName,
        approval.getStatus(),
        approval.getCreatedAt(),
        approvalLineDTOS);
  }

  @AuditLog(actionType = "PROCESS", targetName = "APPROVAL")
  @Transactional
  public void processApproval(Long approvalCode, ApprovalProcessDTO approvalProcessDTO) {
    log.info("[ApprovalService] processApproval Start");

    String memberId = securityUtil.getCurrentMemberId();

    Member approver = securityUtil.findMemberById(memberId);

    Approval approval =
        approvalRepository
            .findById(approvalCode)
            .orElseThrow(() -> new CustomException(ErrorCode.APPROVAL_NOT_FOUND));

    ApprovalLine myLine =
        approval.getApprovalLines().stream()
            .filter(line -> line.getApprover().getMemberCode() == approver.getMemberCode())
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_APPROVAL));

    if (myLine.getStatus() != ApprovalStatus.PENDING) {
      throw new CustomException(ErrorCode.INVALID_APPROVAL_STATE);
    }

    myLine.processApproval(approvalProcessDTO.status(), approvalProcessDTO.comments());

    if (approvalProcessDTO.status() == ApprovalStatus.REJECTED) {
      approval.changeStatus(ApprovalStatus.REJECTED);
      log.info("[ApprovalService] 결재 문서가 반려되었습니다.");

      notificationProducer.sendNotification(
          approval.getMember().getMemberId(),
          "결재 문서가 반려되었습니다. : " + approval.getTitle(),
          "/approvals/" + approval.getApprovalCode());

    } else if (approvalProcessDTO.status() == ApprovalStatus.APPROVED) {
      int netxtOrder = myLine.getLineOrder() + 1;

      ApprovalLine nextLine =
          approval.getApprovalLines().stream()
              .filter(line -> line.getLineOrder() == netxtOrder)
              .findFirst()
              .orElse(null);

      if (nextLine != null) {
        nextLine.processApproval(ApprovalStatus.PENDING, null);
        log.info("[ApprovalService] 다음 결재자로 넘어갔습니다.");

        notificationProducer.sendNotification(
            nextLine.getApproval().getMember().getMemberId(),
            "결재 요청 문서가 도착했습니다. : " + approval.getTitle(),
            "/approvals/" + approval.getApprovalCode());

      } else {
        approval.changeStatus(ApprovalStatus.APPROVED);

        if (approval.getApprovalType() == ApprovalType.VACATION
            && approval.getStartDate() != null
            && approval.getEndDate() != null) {

          Member drafter = approval.getMember();

          long days = ChronoUnit.DAYS.between(approval.getStartDate(), approval.getEndDate()) + 1;

          drafter.useVacation((double) days);

          attendanceService.registerVacation(
              drafter, approval.getStartDate(), approval.getEndDate());

          log.info("[ApprovalService] 휴가 등록이 완료되었습니다. {}일 차감", days);
        }

        log.info("[ApprovalService] 최종 승인이 완료되었습니다.");

        notificationProducer.sendNotification(
            approval.getMember().getMemberId(),
            "결재가 최종 승인되었습니다. : " + approval.getTitle(),
            "/approvals/" + approval.getApprovalCode());
      }
    }
    log.info("[ApprovalService] processApproval End");
  }
}
