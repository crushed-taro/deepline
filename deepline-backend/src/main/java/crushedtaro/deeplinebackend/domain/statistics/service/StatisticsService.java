package crushedtaro.deeplinebackend.domain.statistics.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.approval.repository.ApprovalRepository;
import crushedtaro.deeplinebackend.domain.attendance.enums.AttendanceStatus;
import crushedtaro.deeplinebackend.domain.attendance.repository.AttendanceRepository;
import crushedtaro.deeplinebackend.domain.statistics.dto.ApprovalStatDTO;
import crushedtaro.deeplinebackend.domain.statistics.dto.AttendanceStatDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

  private final ApprovalRepository approvalRepository;
  private final AttendanceRepository attendanceRepository;
  private final DataSourcePoolMetadataProvidersConfiguration
      dataSourcePoolMetadataProvidersConfiguration;

  public List<ApprovalStatDTO> getApprovalStatusStats() {
    List<Object[]> results = approvalRepository.countApprovalsByStatus();

    return results.stream()
        .map(row -> new ApprovalStatDTO((ApprovalStatus) row[0], (Long) row[1]))
        .collect(Collectors.toList());
  }

  public List<AttendanceStatDTO> getMonthlyAttendanceStats(int year, int month) {
    List<Object[]> results = attendanceRepository.findDailyStatistics(year, month);

    Map<LocalDate, Map<AttendanceStatus, Long>> dateMap = new LinkedHashMap<>();

    for (Object[] row : results) {
      LocalDate date = (LocalDate) row[0];
      AttendanceStatus status = (AttendanceStatus) row[1];
      Long count = (Long) row[2];

      dateMap.putIfAbsent(date, new HashMap<>());
      dateMap.get(date).put(status, count);
    }

    List<AttendanceStatDTO> stats = new ArrayList<>();
    for (Map.Entry<LocalDate, Map<AttendanceStatus, Long>> entry : dateMap.entrySet()) {
      LocalDate date = entry.getKey();
      Map<AttendanceStatus, Long> counts = entry.getValue();

      stats.add(
          new AttendanceStatDTO(
              date,
              counts.getOrDefault(AttendanceStatus.PRESENT, 0L),
              counts.getOrDefault(AttendanceStatus.LATE, 0L),
              counts.getOrDefault(AttendanceStatus.VACATION, 0L),
              counts.getOrDefault(AttendanceStatus.ABSENT, 0L)));
    }
    return stats;
  }
}
