package crushedtaro.deeplinebackend.domain.member.entity;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

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

  @OneToMany
  @JoinColumn(name = "member_code")
  private List<MemberRole> memberRole;
}
