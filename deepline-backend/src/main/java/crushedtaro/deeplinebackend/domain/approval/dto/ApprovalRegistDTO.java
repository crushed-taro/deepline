package crushedtaro.deeplinebackend.domain.approval.dto;

import java.util.List;

public record ApprovalRegistDTO(String title, String content, List<Integer> approverCodes) {}
