package crushedtaro.deeplinebackend.domain.member.dto;

import crushedtaro.deeplinebackend.domain.member.enums.LoginStatus;

public record LoginResponseDTO(int memberId, LoginStatus loginStatus, String message) {}
