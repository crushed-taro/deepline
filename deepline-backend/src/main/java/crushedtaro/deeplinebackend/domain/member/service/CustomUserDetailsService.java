package crushedtaro.deeplinebackend.domain.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.AuthorityDTO;
import crushedtaro.deeplinebackend.domain.member.dto.MemberDTO;
import crushedtaro.deeplinebackend.domain.member.dto.MemberRoleDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.entity.MemberRole;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("[CustomUserDetailsService] loadUserByUsername -> username: {}", username);

    Member memberEntity = memberRepository.findByMemberId(username);

    List<GrantedAuthority> authorities = new ArrayList<>();
    for (MemberRole role : memberEntity.getMemberRole()) {
      String authorityName = role.getAuthority().getAuthorityName();
      authorities.add(new SimpleGrantedAuthority(authorityName));
    }

    List<MemberRoleDTO> memberRoleDTODTOS =
        memberEntity.getMemberRole().stream()
            .map(
                role ->
                    new MemberRoleDTO(
                        role.getMemberNo(),
                        role.getAuthorityCode(),
                        new AuthorityDTO(
                            role.getAuthority().getAuthorityCode(),
                            role.getAuthority().getAuthorityName(),
                            role.getAuthority().getAuthorityDesc())))
            .collect(Collectors.toList());

    return new MemberDTO(
        memberEntity.getMemberCode(),
        memberEntity.getMemberId(),
        memberEntity.getMemberPassword(),
        memberEntity.getMemberName(),
        memberEntity.getMemberEmail(),
        memberRoleDTODTOS,
        authorities);
  }
}
