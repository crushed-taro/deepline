package crushedtaro.deeplinebackend.domain.member.service;

import crushedtaro.deeplinebackend.domain.member.dto.ResetPasswordDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.FindIdDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public String findMemberId(FindIdDTO findIdDTO) {
    log.info("[MemberService] findMemberId() START");

    Member member =
        memberRepository
            .findByMemberNameAndMemberEmail(findIdDTO.memberName(), findIdDTO.memberEmail())
            .orElseThrow(() -> new RuntimeException("일치하는 회원 정보가 없습니다."));

    log.info("[MemberService] findMemberid() END");

    return member.getMemberId();
  }

  @Transactional
  public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
    log.info("[MemberService] resetPassword() START");

    Member member = memberRepository.findByMemberIdAndMemberNameAndMemberEmail(
            resetPasswordDTO.memberId(),
            resetPasswordDTO.memberName(),
            resetPasswordDTO.memberEmail()
    ).orElseThrow(
            () -> new RuntimeException("일치하는 회원 정보가 없습니다.")
    );

    if(!resetPasswordDTO.isPasswordMatched()) {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String encodedPassword = passwordEncoder.encode(resetPasswordDTO.newPassword());
    log.debug("[MemberService] Generated Temp Password : {}", encodedPassword);

    member.changePassword(encodedPassword);

    log.info("[MemberService] resetPassword() END");
    return encodedPassword;
  }
}
