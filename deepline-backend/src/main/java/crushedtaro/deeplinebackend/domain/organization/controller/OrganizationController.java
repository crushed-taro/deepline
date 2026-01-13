package crushedtaro.deeplinebackend.domain.organization.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;
import crushedtaro.deeplinebackend.domain.organization.enums.OrganizationStatus;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;
import crushedtaro.deeplinebackend.domain.organization.repository.PositionRepository;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Organization API", description = "부서 및 직급 관리 (관리자 전용)")
public class OrganizationController {

  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;

  @GetMapping("/departments")
  @Operation(summary = "부서 목록 조회", description = "전체 부서 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<Department>>> getDepartments() {
    return BaseResponseFactory.success(
        OrganizationStatus.READ_DEPARTMENT_SUCCESS, departmentRepository.findAll());
  }

  @PostMapping("/departments")
  @Operation(summary = "부서 생성", description = "새로운 부서를 생성합니다.")
  public ResponseEntity<BaseResponse<Department>> createDepartment(
      @RequestBody Department department) {
    return BaseResponseFactory.success(
        OrganizationStatus.CREATE_DEPARTMENT_SUCCESS, departmentRepository.save(department));
  }

  @DeleteMapping("/departments/{id}")
  @Operation(summary = "부서 삭제", description = "특정 부서를 삭제합니다.")
  public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Long id) {
    departmentRepository.deleteById(Math.toIntExact(id));
    return BaseResponseFactory.success(OrganizationStatus.DELETE_DEPARTMENT_SUCCESS);
  }

  @GetMapping("/positions")
  @Operation(summary = "직급 목록 조회", description = "전체 직급 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<Position>>> getPositions() {
    return BaseResponseFactory.success(
        OrganizationStatus.READ_POSITION_SUCCESS, positionRepository.findAll());
  }

  @PostMapping("/positions")
  @Operation(summary = "직급 생성", description = "새로운 직급을 생성합니다.")
  public ResponseEntity<BaseResponse<Position>> createPosition(@RequestBody Position position) {
    return BaseResponseFactory.success(
        OrganizationStatus.CREATE_POSITION_SUCCESS, positionRepository.save(position));
  }
}
