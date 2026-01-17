package crushedtaro.deeplinebackend.domain.member.dto;

import java.time.LocalDateTime;

public record MemberResponseDTO(
    int memberCode,
    String memberId,
    String memberName,
    String memberEmail,
    double remainVacation,
    LocalDateTime createdAt,
    String deptName,
    String positionName,
    String profileUrl) {}
