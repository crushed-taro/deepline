package crushedtaro.deeplinebackend.domain.approval.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
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
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.notification.service.NotificationProducer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalService {

  private final ApprovalRepository approvalRepository;
  private final MemberRepository memberRepository;
  private final AttendanceRepository attendanceRepository;
  private final AttendanceService attendanceService;
  private final NotificationProducer notificationProducer;

  @Transactional
  public void registerApproval(ApprovalRegistDTO approvalRegistDTO) {
    log.info("[ApprovalService] registerApproval Start");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    ApprovalType type = ApprovalType.GENERAL;
    if (approvalRegistDTO.type() != null) {
      try {
        type = ApprovalType.valueOf(approvalRegistDTO.type());
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("유효하지 않은 결재 유형입니다.");
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
      throw new RuntimeException("최소 1명 이상의 결재자를 지정해야 합니다.");
    }

    for (int i = 0; i < approverCodes.size(); i++) {

      int approverCode = approverCodes.get(i);

      Member approver =
          memberRepository
              .findById(approverCode)
              .orElseThrow(() -> new RuntimeException("결재자 정보가 없습니다."));

      if (approver.getMemberCode() == member.getMemberCode()) {
        throw new RuntimeException("본인은 결재자로 지정할 수 없습니다.");
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
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

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
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

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
            .orElseThrow(() -> new RuntimeException("해당 결재 문서가 존재하지 않습니다."));

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

  @Transactional
  public void processApproval(Long approvalCode, ApprovalProcessDTO approvalProcessDTO) {
    log.info("[ApprovalService] processApproval Start");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    Member approver =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    Approval approval =
        approvalRepository
            .findById(approvalCode)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 결재 문서입니다."));

    ApprovalLine myLine =
        approval.getApprovalLines().stream()
            .filter(line -> line.getApprover().getMemberCode() == approver.getMemberCode())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("결재 권한이 없습니다."));

    if (myLine.getStatus() != ApprovalStatus.PENDING) {
      throw new RuntimeException("현재 결재할 수 있는 상태가 아닙니다.");
    }

    myLine.processApproval(approvalProcessDTO.status(), approvalProcessDTO.comments());

    if (approvalProcessDTO.status() == ApprovalStatus.REJECTED) {
      approval.changeStatus(ApprovalStatus.REJECTED);
      log.info("[ApprovalService] 결재 문서가 반려되었습니다.");
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
      }
    }
    log.info("[ApprovalService] processApproval End");
  }
}
