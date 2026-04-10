package crushedtaro.deeplinebackend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<BaseResponse<Void>> handlerCustomException(CustomException e) {
    log.error("handlerCustomException Throw CustomException : {}", e.getErrorCode());
    return BaseResponseFactory.create(e.getErrorCode());
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<BaseResponse<Void>> handleRuntimeException(RuntimeException e) {
    log.error("handleRuntimeException Throw RuntimeException : {}", e.getMessage(), e);

    BaseResponse<Void> response =
        new BaseResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "RUNTIME_ERROR",
            "서버 처리 중 예기치 못한 문제가 발생했습니다: " + e.getMessage(),
            null);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
    log.error("handleException throw Exception : {}", e.getMessage());
    return BaseResponseFactory.create(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
