package crushedtaro.deeplinebackend.domain.member.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_member_role")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MemberRolePk.class)
public class MemberRole {

  @Id
  @Column(name = "member_code")
  private int memberNo;

  @Id
  @Column(name = "authority_code")
  private int authorityCode;

  @ManyToOne
  @JoinColumn(name = "authority_code", insertable = false, updatable = false)
  private Authority authority;
}
