package crushedtaro.deeplinebackend.domain.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeRequestDTO;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeResponseDTO;
import crushedtaro.deeplinebackend.domain.notice.entity.Notice;
import crushedtaro.deeplinebackend.domain.notice.repository.NoticeRepository;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final MemberRepository memberRepository;

  public void createNotice(NoticeRequestDTO noticeRequestDTO) {
    log.info("[NoticeService] createNotice() START");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member author =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    Notice notice =
        Notice.builder()
            .title(noticeRequestDTO.title())
            .content(noticeRequestDTO.content())
            .isPinned(noticeRequestDTO.isPinned())
            .author(author)
            .build();

    noticeRepository.save(notice);
    log.info("[NoticeService] createNotice() END");
  }

  public Page<NoticeResponseDTO> getNoticeList(Pageable pageable) {
    return noticeRepository
        .findAllByIsDeletedOrderByIsPinnedDescCreatedAtDesc("N", pageable)
        .map(NoticeResponseDTO::from);
  }

  public NoticeResponseDTO getNoticeDetail(Long noticeCode) {
    Notice notice =
        noticeRepository
            .findById(noticeCode)
            .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
    notice.incrementViewCount();
    return NoticeResponseDTO.from(notice);
  }

  public void deleteNotice(Long noticeCode) {
    log.info("[NoticeService] deleteNotice() START");

    Notice notice =
        noticeRepository
            .findById(noticeCode)
            .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

    notice.withdraw();
    log.info("[NoticeService] deleteNotice() END");
  }
}
