package crushedtaro.deeplinebackend.domain.common;

import org.springframework.http.HttpStatus;

public interface ResponseStatus {
  HttpStatus getStatus();

  String getCode();

  String getMessage();
}
