package crushedtaro.deeplinebackend.domain.member.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum TokenStatus implements ResponseStatus {
  LOGIN_SUCCESS(HttpStatus.OK, "LOGIN_001", "로그인이 성공적으로 완료되었습니다."),
  LOGIN_FALSE(HttpStatus.BAD_REQUEST, "LOGIN_002", "로그인에 실패하였습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
