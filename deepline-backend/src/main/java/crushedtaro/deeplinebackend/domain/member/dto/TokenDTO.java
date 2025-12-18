package crushedtaro.deeplinebackend.domain.member.dto;

import crushedtaro.deeplinebackend.domain.member.enums.TokenStatus;

public record TokenDTO(
    String grantType,
    String memberName,
    String accessToken,
    Long accessTokenExpiresIn,
    TokenStatus tokenStatus) {}
