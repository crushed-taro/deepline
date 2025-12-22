package crushedtaro.deeplinebackend.domain.attendance.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum AttendanceResponseStatus implements ResponseStatus {
  CLOCK_IN_SUCCESS(HttpStatus.OK, "ATT_001", "출근 처리가 완료되었습니다"),
  CLOCK_OUT_SUCCESS(HttpStatus.OK, "ATT_002", "퇴근 처리가 완료되었습니다");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
