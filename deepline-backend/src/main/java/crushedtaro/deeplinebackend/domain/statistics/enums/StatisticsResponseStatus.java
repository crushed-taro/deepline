package crushedtaro.deeplinebackend.domain.statistics.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum StatisticsResponseStatus implements ResponseStatus {
  READ_APPR_SUCCESS(HttpStatus.OK, "STAT_001", "결재 상태 통계 조회 성공"),
  READ_ATTE_SUCCESS(HttpStatus.OK, "STAT_002", "월별 근태 통계 조회 성공"),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
