package crushedtaro.deeplinebackend.domain.approval.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Table(name = "tbl_approval")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Approval extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "approval_code")
  private Long approvalCode;

  @Column(name = "approval_title", nullable = false)
  private String title;

  @Column(name = "approval_content", columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_code", nullable = false)
  private Member member;

  @Enumerated(EnumType.STRING)
  @Column(name = "approval_status")
  @Builder.Default
  private ApprovalStatus status = ApprovalStatus.PENDING;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @OneToMany(mappedBy = "approval", cascade = CascadeType.ALL)
  @Builder.Default
  private List<ApprovalLine> approvalLine = new ArrayList<>();

  public void changeStatus(ApprovalStatus status) {
    this.status = status;
    if (status == ApprovalStatus.APPROVED || status == ApprovalStatus.REJECTED) {
      this.completedAt = LocalDateTime.now();
    }
  }
}
