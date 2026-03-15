package crushedtaro.deeplinebackend.domain.member.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record MemberDTO(
    Integer memberCode,
    String memberId,
    String memberPassword,
    String memberName,
    String memberEmail,
    List<MemberRoleDTO> memberRoleDTO,
    Collection<GrantedAuthority> authorities)
    implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return memberPassword;
  }

  @Override
  public String getUsername() {
    return memberId;
  }
}
