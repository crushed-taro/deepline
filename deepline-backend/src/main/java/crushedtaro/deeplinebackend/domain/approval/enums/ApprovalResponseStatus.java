package crushedtaro.deeplinebackend.domain.approval.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum ApprovalResponseStatus implements ResponseStatus {
  APPROVAL_REGIST_SUCCESS(HttpStatus.CREATED, "APP_001", "결재 문서가 상신되었습니다."),
  READ_PROFILE_SUCCESS(HttpStatus.OK, "APP_002", "상신목록 조회를 성공했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
