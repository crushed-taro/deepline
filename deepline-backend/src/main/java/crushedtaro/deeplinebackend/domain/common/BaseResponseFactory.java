package crushedtaro.deeplinebackend.domain.common;

import org.springframework.http.ResponseEntity;

public class BaseResponseFactory {

  public static <T> ResponseEntity<BaseResponse<T>> create(ResponseStatus status, T result) {
    return ResponseEntity.status(status.getStatus())
        .body(
            new BaseResponse<>(status.getStatus(), status.getCode(), status.getMessage(), result));
  }

  public static ResponseEntity<BaseResponse<Void>> create(ResponseStatus status) {
    return ResponseEntity.status(status.getStatus())
        .body(new BaseResponse<>(status.getStatus(), status.getCode(), status.getMessage(), null));
  }

  public static <T> ResponseEntity<BaseResponse<T>> success(ResponseStatus status, T result) {
    return ResponseEntity.status(status.getStatus())
        .body(
            new BaseResponse<>(status.getStatus(), status.getCode(), status.getMessage(), result));
  }

  public static ResponseEntity<BaseResponse<Void>> success(ResponseStatus status) {
    return ResponseEntity.status(status.getStatus())
        .body(new BaseResponse<>(status.getStatus(), status.getCode(), status.getMessage(), null));
  }
}
