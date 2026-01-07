package crushedtaro.deeplinebackend.domain.approval.dto;

import java.time.LocalDate;
import java.util.List;

public record ApprovalRegistDTO(
    String title,
    String content,
    List<Integer> approverCodes,
    String type,
    LocalDate startDate,
    LocalDate endDate) {}
