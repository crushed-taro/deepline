package crushedtaro.deeplinebackend.domain.common;

import org.springframework.http.HttpStatus;

public record BaseResponse<T>(HttpStatus status, String code, String message, T result) {}
