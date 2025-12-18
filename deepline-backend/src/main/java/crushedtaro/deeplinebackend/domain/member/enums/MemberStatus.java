package crushedtaro.deeplinebackend.domain.member.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements ResponseStatus {
  FIND_ID_SUCCESS(HttpStatus.OK, "MEMBER_001", "아이디 찾기에 성공하였습니다."),
  RESET_PASSWORD_SUCCESS(HttpStatus.OK, "MEMBER_002", "비밀번호 변경에 성공하였습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
