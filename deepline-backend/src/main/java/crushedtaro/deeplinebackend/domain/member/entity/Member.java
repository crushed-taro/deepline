package crushedtaro.deeplinebackend.domain.member.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;

@Entity
@Table(name = "tbl_member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id
  @Column(name = "member_code")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int memberCode;

  @Column(name = "member_id")
  private String memberId;

  @Column(name = "member_password")
  private String memberPassword;

  @Column(name = "member_name")
  private String memberName;

  @Column(name = "member_email")
  private String memberEmail;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private String isDeleted = "N";

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany
  @JoinColumn(name = "member_code")
  private List<MemberRole> memberRole;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dept_code")
  private Department department;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_code")
  private Position position;

  public void changePassword(String newPassword) {
    this.memberPassword = newPassword;
  }

  public void withdraw() {
    this.isDeleted = "Y";
    this.deletedAt = LocalDateTime.now();
  }

  public void updateMemberInfo(String memberName, String memberEmail) {
    this.memberName = memberName;
    this.memberEmail = memberEmail;
  }

  public void assignDepartment(Department department) {
    this.department = department;
  }

  public void assignPosition(Position position) {
    this.position = position;
  }
}
