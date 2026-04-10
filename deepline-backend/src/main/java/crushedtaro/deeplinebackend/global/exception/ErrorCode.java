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
  INSUFFICIENT_VACATION_DAYS(HttpStatus.BAD_REQUEST, "M005", "잔여 휴가 일수가 부족합니다."),
  PROFILE_IMAGE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "M006", "프로필 이미지 저장 중 오류가 발생했습니다."),

  DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ORG_001", "해당 부서를 찾을 수 없습니다."),
  DUPLICATE_DEPARTMENT_NAME(HttpStatus.BAD_REQUEST, "ORG_002", "이미 존재하는 부서 이름입니다."),
  POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "ORG_003", "해당 직급을 찾을 수 없습니다."),

  ALREADY_ATTENDED(HttpStatus.CONFLICT, "ATT_001", "이미 출근 처리가 되었습니다."),
  ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ATT_002", "출근 기록을 찾을 수 없습니다."),

  INVALID_APPROVAL_TYPE(HttpStatus.BAD_REQUEST, "APP_001", "유효하지 않은 결재 유형입니다."),
  APPROVER_REQUIRED(HttpStatus.BAD_REQUEST, "APP_002", "최소 1명 이상의 결재자를 지정해야 합니다."),
  APPROVER_NOT_FOUND(HttpStatus.NOT_FOUND, "APP_003", "결재자 정보를 찾을 수 없습니다."),
  CANNOT_APPROVE_SELF(HttpStatus.BAD_REQUEST, "APP_004", "본인은 결재자로 지정할 수 없습니다."),
  APPROVAL_NOT_FOUND(HttpStatus.NOT_FOUND, "APP_005", "해당 결재 문서를 찾을 수 없습니다."),
  UNAUTHORIZED_APPROVAL(HttpStatus.FORBIDDEN, "APP_006", "결재 권한이 없습니다."),
  INVALID_APPROVAL_STATE(HttpStatus.BAD_REQUEST, "APP_007", "현재 결재할 수 있는 상태가 아닙니다."),

  CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHT_001", "채팅방을 찾을 수 없습니다."),
  SENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHT_002", "발신자 정보를 찾을 수 없습니다."),
  RECEIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHT_003", "수신자 정보를 찾을 수 없습니다."),

  TOKEN_WITHOUT_AUTHORITY(HttpStatus.UNAUTHORIZED, "AUTH_001", "권한 정보가 없는 토큰입니다."),
  UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "AUTH_002", "인증 정보가 존재하지 않습니다."),
  WEBSOCKET_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "AUTH_003", "웹소켓 연결 인증에 실패했습니다."),

  NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_001", "공지사항을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
