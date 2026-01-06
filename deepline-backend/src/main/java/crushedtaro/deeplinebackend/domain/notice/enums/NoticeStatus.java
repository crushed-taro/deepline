package crushedtaro.deeplinebackend.domain.notice.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum NoticeStatus implements ResponseStatus {
  CREATE_NOTICE_SUCCESS(HttpStatus.OK, "NOTICE_001", "공지 생성 성공"),
  READ_LIST_SUCCESS(HttpStatus.OK, "NOTICE_002", "공지 목록 조회 성공"),
  READ_DETAIL_SUCCESS(HttpStatus.OK, "NOTICE_003", "공지 세부 조회 성공"),
  WITHDRAW_NOTICE_SUCCESS(HttpStatus.OK, "NOTICE_004", "공지 삭제 성공");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
