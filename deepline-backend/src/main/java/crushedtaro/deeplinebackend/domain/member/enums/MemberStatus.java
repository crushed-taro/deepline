package crushedtaro.deeplinebackend.domain.member.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements ResponseStatus {
  FIND_ID_SUCCESS(HttpStatus.OK, "MEMBER_001", "아이디 찾기에 성공하였습니다."),
  RESET_PASSWORD_SUCCESS(HttpStatus.OK, "MEMBER_002", "비밀번호 변경에 성공하였습니다."),
  READ_PROFILE_SUCCESS(HttpStatus.OK, "MEMBER_003", "내 정보 조회 성공"),
  WITHDRAW_SUCCESS(HttpStatus.OK, "MEMBER_004", "회원 탈퇴 성공"),
  UPDATE_INFO_SUCCESS(HttpStatus.OK, "MEMBER_005", "회원 정보 수정 성공"),
  ASSIGN_SUCCESS(HttpStatus.OK, "MEMBER_006", "회원 권한 수정 성공"),
  READ_LIST_SUCCESS(HttpStatus.OK, "MEMBER_007", "회원 목록 조회 성공");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
