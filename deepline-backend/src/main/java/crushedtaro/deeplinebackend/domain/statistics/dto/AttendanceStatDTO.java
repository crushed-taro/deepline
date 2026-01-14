package crushedtaro.deeplinebackend.domain.statistics.dto;

import java.time.LocalDate;

public record AttendanceStatDTO(
    LocalDate date, long present, long late, long vacation, long absent) {}
