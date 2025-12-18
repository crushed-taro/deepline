package crushedtaro.deeplinebackend.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_authority")
public class Authority {

  @Id
  @Column(name = "authority_code")
  private int authorityCode;

  @Column(name = "authority_name")
  private String authorityName;

  @Column(name = "authority_desc")
  private String authorityDesc;
}
