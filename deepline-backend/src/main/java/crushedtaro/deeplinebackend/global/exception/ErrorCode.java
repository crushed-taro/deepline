package crushedtaro.deeplinebackend.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ResponseStatus {
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),

  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "M002", "이미 사용 중인 이메일입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M003", "비밀번호가 일치하지 않습니다."),
  WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN, "M004", "탈퇴한 회원입니다. 로그인이 불가합니다."),

  DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ORG_001", "해당 부서를 찾을 수 없습니다."),
  DUPLICATE_DEPARTMENT_NAME(HttpStatus.BAD_REQUEST, "ORG_002", "이미 존재하는 부서 이름입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
