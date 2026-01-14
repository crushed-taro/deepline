package crushedtaro.deeplinebackend.domain.statistics.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.statistics.dto.ApprovalStatDTO;
import crushedtaro.deeplinebackend.domain.statistics.dto.AttendanceStatDTO;
import crushedtaro.deeplinebackend.domain.statistics.enums.StatisticsResponseStatus;
import crushedtaro.deeplinebackend.domain.statistics.service.StatisticsService;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics API", description = "관리자 대시보드 통계")
public class StatisticsController {

  private final StatisticsService statisticsService;

  @GetMapping("/approvals")
  public ResponseEntity<BaseResponse<List<ApprovalStatDTO>>> getApprovalStats() {
    return BaseResponseFactory.success(
        StatisticsResponseStatus.READ_APPR_SUCCESS, statisticsService.getApprovalStatusStats());
  }

  @GetMapping("/attendances")
  public ResponseEntity<BaseResponse<List<AttendanceStatDTO>>> getAttendanceStats(
      @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
    LocalDate now = LocalDate.now();
    int targetYear = (year != null) ? year : now.getYear();
    int targetMonth = (month != null) ? month : now.getMonthValue();

    return BaseResponseFactory.success(
        StatisticsResponseStatus.READ_ATTE_SUCCESS,
        statisticsService.getMonthlyAttendanceStats(targetYear, targetMonth));
  }
}
