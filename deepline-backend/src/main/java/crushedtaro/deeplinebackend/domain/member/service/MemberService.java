package crushedtaro.deeplinebackend.domain.member.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.FindIdDTO;
import crushedtaro.deeplinebackend.domain.member.dto.MemberResponseDTO;
import crushedtaro.deeplinebackend.domain.member.dto.ResetPasswordDTO;
import crushedtaro.deeplinebackend.domain.member.dto.UpdateMemberDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

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

    Member member =
        memberRepository
            .findByMemberIdAndMemberNameAndMemberEmail(
                resetPasswordDTO.memberId(),
                resetPasswordDTO.memberName(),
                resetPasswordDTO.memberEmail())
            .orElseThrow(() -> new RuntimeException("일치하는 회원 정보가 없습니다."));

    if (!resetPasswordDTO.isPasswordMatched()) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String encodedPassword = passwordEncoder.encode(resetPasswordDTO.newPassword());
    log.debug("[MemberService] Generated Temp Password : {}", encodedPassword);

    member.changePassword(encodedPassword);

    log.info("[MemberService] resetPassword() END");
    return encodedPassword;
  }

  @Transactional(readOnly = true)
  public MemberResponseDTO getMyInfo() {
    log.info("[MemberService] getMyInfo() START");

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
      throw new RuntimeException("인증 정보가 없습니다.");
    }

    String memberId = authentication.getName();
    log.info("[MemberService] Current User ID : {}", memberId);

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    log.info("[MemberService] getMyInfo() END");

    return new MemberResponseDTO(
        member.getMemberCode(),
        member.getMemberId(),
        member.getMemberName(),
        member.getMemberEmail());
  }

  @Transactional
  public void withdraw() {
    log.info("[MemberService] withdraw() START");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    log.info("[MemberService] Current User ID : {}", memberId);

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    member.withdraw();

    log.info("[MemberService] withdraw() END");
  }

  @Transactional
  public void updateMyInfo(UpdateMemberDTO updateMemberDTO) {
    log.info("[MemberService] updateMyInfo() START");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    String newEmail = updateMemberDTO.memberEmail();
    if (newEmail != null && !newEmail.equals(member.getMemberEmail())) {
      if (memberRepository.existsByMemberEmail(newEmail)) {
        throw new RuntimeException("이미 사용 중인 이메일입니다.");
      }
    }

    member.updateMemberInfo(updateMemberDTO.memberName(), newEmail);

    log.info("[MemberService] updateMyInfo() END");
  }
}
