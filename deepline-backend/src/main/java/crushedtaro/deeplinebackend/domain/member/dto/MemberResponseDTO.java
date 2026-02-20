package crushedtaro.deeplinebackend.domain.member.dto;

import java.time.LocalDateTime;

import crushedtaro.deeplinebackend.domain.member.entity.Member;

public record MemberResponseDTO(
    int memberCode,
    String memberId,
    String memberName,
    String memberEmail,
    double remainVacation,
    LocalDateTime createdAt,
    String deptName,
    String positionName,
    String profileUrl) {

  public static MemberResponseDTO from(Member member) {
    String deptName =
        (member.getDepartment() != null) ? member.getDepartment().getDeptName() : "미배정";
    String positionName =
        (member.getPosition() != null) ? member.getPosition().getPositionName() : "미배정";

    return new MemberResponseDTO(
        member.getMemberCode(),
        member.getMemberId(),
        member.getMemberName(),
        member.getMemberEmail(),
        member.getRemainVacation(),
        member.getCreatedAt(),
        deptName,
        positionName,
        member.getProfileUrl());
  }
}
