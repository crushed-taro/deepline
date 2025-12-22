package crushedtaro.deeplinebackend.domain.attendance.dto;

import java.time.LocalDate;

import crushedtaro.deeplinebackend.domain.attendance.enums.AttendanceStatus;

public record AttendanceResponseDTO(
    LocalDate workDate, String startTime, String endTime, AttendanceStatus status) {}
