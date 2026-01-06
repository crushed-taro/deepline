package crushedtaro.deeplinebackend.domain.approval.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalType {
  GENERAL("일반 결재"),
  VACATION("휴가 신청");

  private final String description;
}
