package crushedtaro.deeplinebackend.domain.member.dto;

public record MemberResponseDTO(
    int memberCode,
    String memberId,
    String memberName,
    String memberEmail,
    double remainVacation,
    String deptName,
    String positionName) {}
