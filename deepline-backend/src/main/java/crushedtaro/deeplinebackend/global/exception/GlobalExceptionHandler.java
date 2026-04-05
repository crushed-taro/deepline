package crushedtaro.deeplinebackend.global.exception;

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

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
    log.error("handleException throw Exception : {}", e.getMessage());
    return BaseResponseFactory.create(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
