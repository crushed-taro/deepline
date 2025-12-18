package crushedtaro.deeplinebackend.domain.member.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum SignupStatus implements ResponseStatus {
  SIGNUP_SUCCESS(HttpStatus.CREATED, "SIGNUP_001", "회원가입이 성공적으로 완료되었습니다."),
  SIGNUP_FALSE(HttpStatus.BAD_REQUEST, "SIGNUP_002", "회원가입에 실패하였습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
