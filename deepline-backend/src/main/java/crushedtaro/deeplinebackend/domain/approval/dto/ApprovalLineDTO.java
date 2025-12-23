package crushedtaro.deeplinebackend.domain.approval.dto;

import java.time.LocalDateTime;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;

public record ApprovalLineDTO(
    int lineOrder,
    String approverName,
    String positionName,
    ApprovalStatus status,
    String comment,
    LocalDateTime processedAt) {}
