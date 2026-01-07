package crushedtaro.deeplinebackend.domain.organization.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.ResponseStatus;

@Getter
@RequiredArgsConstructor
public enum OrganizationStatus implements ResponseStatus {
  READ_DEPARTMENT_SUCCESS(HttpStatus.OK, "DEPARTMENT_001", "부서 정보 조회 성공"),
  CREATE_DEPARTMENT_SUCCESS(HttpStatus.OK, "DEPARTMENT_002", "부서 생성 성공"),
  DELETE_DEPARTMENT_SUCCESS(HttpStatus.OK, "DEPARTMENT_003", "부서 삭제 성공"),
  READ_POSITION_SUCCESS(HttpStatus.OK, "POSITION_001", "직급 정보 조회 성공"),
  CREATE_POSITION_SUCCESS(HttpStatus.OK, "POSITION_002", "직급 생성 성공");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
