package crushedtaro.deeplinebackend.domain.statistics.dto;

import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;

public record ApprovalStatDTO(ApprovalStatus status, long count) {}
