package crushedtaro.deeplinebackend.domain.approval.dto;

import java.time.LocalDateTime;
import java.util.List;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;

public record ApprovalDetailDTO(
    Long approvalCode,
    String title,
    String content,
    String drafterName,
    String deptName,
    ApprovalStatus status,
    LocalDateTime createdAt,
    List<ApprovalLineDTO> approvalLines) {}
