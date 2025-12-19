package crushedtaro.deeplinebackend.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.MemberDTO;
import crushedtaro.deeplinebackend.domain.member.dto.SignupResponseDTO;
import crushedtaro.deeplinebackend.domain.member.dto.TokenDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.entity.MemberRole;
import crushedtaro.deeplinebackend.domain.member.enums.SignupStatus;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRoleRepository;
import crushedtaro.deeplinebackend.infra.jwt.TokenProvider;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final MemberRoleRepository memberRoleRepository;
  private final TokenProvider tokenProvider;

  @Transactional
  public SignupResponseDTO signup(MemberDTO member) {
    log.info("[AuthService] signup() START");
    log.info("[AuthService] member : {}", member);

    if (memberRepository.existsByMemberEmail(member.memberEmail())) {
      log.warn("[AuthService] signup() Duplicated Email Found!");
      throw new RuntimeException("이미 존재하는 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(member.memberPassword());

    Member registMember =
        new Member(
            0, member.memberId(), encodedPassword, member.memberName(), member.memberEmail(), null);

    Member savedMember = memberRepository.save(registMember);

    int memberCode = savedMember.getMemberCode();

    MemberRole newRole = MemberRole.builder().memberNo(memberCode).authorityCode(2).build();

    memberRoleRepository.save(newRole);

    log.info("[AuthService] signup() END");

    return new SignupResponseDTO(
        memberCode, SignupStatus.SIGNUP_SUCCESS, savedMember.getMemberEmail());
  }

  @Transactional
  public TokenDTO login(MemberDTO member) {
    log.info("[AuthService] login() START");
    log.info("[AuthService] member : {}", member);

    Member registedUser =
        memberRepository
            .findByMemberId(member.memberId())
            .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));

    if (!memberRepository.existsByMemberId(member.memberId())) {
      log.warn("[AuthService] Login() Required User Not Found!");
      throw new RuntimeException("User Not Found");
    }

    if (!passwordEncoder.matches(member.memberPassword(), registedUser.getMemberPassword())) {
      log.warn("[AuthService] Login() Password Match Failed!");
      throw new RuntimeException("Password Match Failed");
    }

    TokenDTO newTokenDTO = tokenProvider.generateTokenDTO(registedUser);

    return newTokenDTO;
  }
}
