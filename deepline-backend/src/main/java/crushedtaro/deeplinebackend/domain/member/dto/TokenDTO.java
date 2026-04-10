package crushedtaro.deeplinebackend.domain.member.dto;

import crushedtaro.deeplinebackend.domain.member.enums.TokenStatus;

public record TokenDTO(
    String grantType,
    String memberName,
    String accessToken,
    String refreshToken,
    Long accessTokenExpiresIn,
    TokenStatus tokenStatus) {}
