package crushedtaro.deeplinebackend.domain.member.dto;

import crushedtaro.deeplinebackend.domain.member.enums.SignupStatus;

public record SignupResponseDTO(int memberId, SignupStatus signupStatus, String message) {}
