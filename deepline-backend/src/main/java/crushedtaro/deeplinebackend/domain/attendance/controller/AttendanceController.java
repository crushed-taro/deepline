package crushedtaro.deeplinebackend.domain.attendance.controller;

import java.time.LocalDate;
import java.util.List;

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
public class AttendanceController {

  private final AttendanceService attendanceService;

  @PostMapping("/clock-in")
  public ResponseEntity<BaseResponse<Void>> clockIn() {
    log.info("[AttendanceController] clockIn Start");
    attendanceService.clockIn();
    return BaseResponseFactory.success(AttendanceResponseStatus.CLOCK_IN_SUCCESS);
  }

  @PostMapping("/clock-out")
  public ResponseEntity<BaseResponse<Void>> clockOut() {
    log.info("[AttendanceController] clockOut Start");
    attendanceService.clockOut();
    return BaseResponseFactory.success(AttendanceResponseStatus.CLOCK_OUT_SUCCESS);
  }

  @GetMapping("/me")
  public ResponseEntity<BaseResponse<List<AttendanceResponseDTO>>> getMyAttendance(
      @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {

    LocalDate now = LocalDate.now();

    year = (year != null) ? year : now.getYear();
    month = (month != null) ? month : now.getMonthValue();

    List<AttendanceResponseDTO> result = attendanceService.getMyAttendance(year, month);

    return BaseResponseFactory.success(AttendanceResponseStatus.READ_PROFILE_SUCCESS, result);
  }
}
