package crushedtaro.deeplinebackend.domain.approval.dto;

import java.time.LocalDateTime;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;

public record ApprovalListDTO(
    Long approvalCode,
    String title,
    String drafterName,
    ApprovalStatus approvalStatus,
    LocalDateTime createdAt) {}
