package crushedtaro.deeplinebackend.domain.notice.dto;

import java.time.LocalDateTime;

import crushedtaro.deeplinebackend.domain.notice.entity.Notice;

public record NoticeResponseDTO(
    Long noticeCode,
    String title,
    String content,
    String authorName,
    int viewCount,
    boolean isPinned,
    LocalDateTime createAt) {

  public static NoticeResponseDTO from(Notice notice) {
    return new NoticeResponseDTO(
        notice.getNoticeCode(),
        notice.getTitle(),
        notice.getContent(),
        notice.getAuthor().getMemberName(),
        notice.getViewCount(),
        notice.isPinned(),
        notice.getCreatedAt());
  }
}
