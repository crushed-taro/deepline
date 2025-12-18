package crushedtaro.deeplinebackend.domain.member.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRolePk implements Serializable {

  private int memberNo;
  private int authorityCode;
}
