package crushedtaro.deeplinebackend.domain.attendance.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
  PRESENT("정상 출근"),
  LATE("지각"),
  LEAVE_EARLY("조퇴"),
  ABSENT("결근"),
  VACATION("휴가");

  private final String description;
}
