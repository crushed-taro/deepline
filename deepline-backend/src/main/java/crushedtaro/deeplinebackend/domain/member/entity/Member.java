package crushedtaro.deeplinebackend.domain.member.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;

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

  @Column(name = "remain_vacation")
  @Builder.Default
  private double remainVacation = 15.0;

  @Column(name = "profile_url")
  private String profileUrl;

  public void useVacation(double days) {
    if (this.remainVacation < days) {
      throw new CustomException(ErrorCode.INSUFFICIENT_VACATION_DAYS);
    }
    this.remainVacation -= days;
  }

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

  public void updateProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }
}
