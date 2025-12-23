package crushedtaro.deeplinebackend.domain.approval.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalStatus {
  WAITING("대기"),
  PENDING("진행 중"),
  APPROVED("승인"),
  REJECTED("반려");

  private final String description;
}
