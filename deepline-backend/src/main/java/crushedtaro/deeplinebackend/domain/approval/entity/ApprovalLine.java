package crushedtaro.deeplinebackend.domain.approval.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Table(name = "tbl_approval_line")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "line_code")
  private Long lineCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "approval_code")
  private Approval approval;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "approver_code")
  private Member approver;

  @Column(name = "line_order")
  private int lineOrder;

  @Enumerated(EnumType.STRING)
  @Column(name = "line_status")
  @Builder.Default
  private ApprovalStatus status = ApprovalStatus.WAITING;

  @Column(name = "approval_comment")
  private String comment;

  @Column(name = "processed_at")
  private LocalDateTime processedAt;

  public void setApproval(Approval approval) {
    this.approval = approval;
  }

  public void processApproval(ApprovalStatus approvalStatus, String comment) {
    this.status = approvalStatus;
    this.comment = comment;
    this.processedAt = LocalDateTime.now();
  }
}
