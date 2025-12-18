package crushedtaro.deeplinebackend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.FindIdDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

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
}
