package crushedtaro.deeplinebackend.domain.approval.dto;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;

public record ApprovalProcessDTO(ApprovalStatus status, String comments) {}
