package crushedtaro.deeplinebackend.domain.approval.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalDetailDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalLineDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalListDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalRegistDTO;
import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.approval.entity.ApprovalLine;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.approval.repository.ApprovalRepository;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalService {

  private final ApprovalRepository approvalRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void registerApproval(ApprovalRegistDTO approvalRegistDTO) {
    log.info("[ApprovalService] registerApproval Start");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    Approval approval =
        Approval.builder()
            .title(approvalRegistDTO.title())
            .content(approvalRegistDTO.content())
            .member(member)
            .status(ApprovalStatus.PENDING)
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

    approvalRepository.save(approval);

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
}
