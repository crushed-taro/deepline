package crushedtaro.deeplinebackend.domain.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.audit.annotation.AuditLog;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeRequestDTO;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeResponseDTO;
import crushedtaro.deeplinebackend.domain.notice.entity.Notice;
import crushedtaro.deeplinebackend.domain.notice.repository.NoticeRepository;
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;
import crushedtaro.deeplinebackend.global.util.SecurityUtil;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final MemberRepository memberRepository;
  private final SecurityUtil securityUtil;

  @AuditLog(actionType = "CREATE", targetName = "NOTICE")
  public void createNotice(NoticeRequestDTO noticeRequestDTO) {
    log.info("[NoticeService] createNotice() START");

    String memberId = securityUtil.getCurrentMemberId();

    Member author = securityUtil.findMemberById(memberId);

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
    Notice notice = findNoticeById(noticeCode);

    notice.incrementViewCount();
    return NoticeResponseDTO.from(notice);
  }

  @AuditLog(actionType = "DELETE", targetName = "NOTICE", key = "#noticeCode")
  public void deleteNotice(Long noticeCode) {
    log.info("[NoticeService] deleteNotice() START");

    Notice notice = findNoticeById(noticeCode);

    notice.withdraw();
    log.info("[NoticeService] deleteNotice() END");
  }

  private Notice findNoticeById(Long noticeCode) {
    return noticeRepository
        .findById(noticeCode)
        .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));
  }
}
