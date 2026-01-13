package crushedtaro.deeplinebackend.domain.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.attendance.dto.AttendanceResponseDTO;
import crushedtaro.deeplinebackend.domain.attendance.enums.AttendanceResponseStatus;
import crushedtaro.deeplinebackend.domain.attendance.service.AttendanceService;
import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance API", description = "출퇴근 체크 및 근태 기록 조회")
public class AttendanceController {

  private final AttendanceService attendanceService;

  @PostMapping("/clock-in")
  @Operation(summary = "출근 체크", description = "현재 시간으로 출근을 등록합니다.")
  public ResponseEntity<BaseResponse<Void>> clockIn() {
    log.info("[AttendanceController] clockIn Start");
    attendanceService.clockIn();
    return BaseResponseFactory.success(AttendanceResponseStatus.CLOCK_IN_SUCCESS);
  }

  @PostMapping("/clock-out")
  @Operation(summary = "퇴근 체크", description = "현재 시간으로 퇴근을 등록합니다.")
  public ResponseEntity<BaseResponse<Void>> clockOut() {
    log.info("[AttendanceController] clockOut Start");
    attendanceService.clockOut();
    return BaseResponseFactory.success(AttendanceResponseStatus.CLOCK_OUT_SUCCESS);
  }

  @GetMapping("/me")
  @Operation(summary = "내 근태 조회", description = "특정 연/월의 내 근태 기록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<AttendanceResponseDTO>>> getMyAttendance(
      @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {

    LocalDate now = LocalDate.now();

    year = (year != null) ? year : now.getYear();
    month = (month != null) ? month : now.getMonthValue();

    List<AttendanceResponseDTO> result = attendanceService.getMyAttendance(year, month);

    return BaseResponseFactory.success(AttendanceResponseStatus.READ_PROFILE_SUCCESS, result);
  }
}
