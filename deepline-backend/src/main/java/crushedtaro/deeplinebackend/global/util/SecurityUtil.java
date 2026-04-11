package crushedtaro.deeplinebackend.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

  private final MemberRepository memberRepository;

  public String getCurrentMemberId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
    }
    return authentication.getName();
  }

  public Member findMemberById(String memberId) {
    return memberRepository
        .findByMemberId(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }
}
