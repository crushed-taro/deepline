package crushedtaro.deeplinebackend.domain.organization.controller;

import java.util.List;

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
public class OrganizationController {

  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;

  @GetMapping("/departments")
  public ResponseEntity<BaseResponse<List<Department>>> getDepartments() {
    return BaseResponseFactory.success(
        OrganizationStatus.READ_DEPARTMENT_SUCCESS, departmentRepository.findAll());
  }

  @PostMapping("/departments")
  public ResponseEntity<BaseResponse<Department>> createDepartment(
      @RequestBody Department department) {
    return BaseResponseFactory.success(
        OrganizationStatus.CREATE_DEPARTMENT_SUCCESS, departmentRepository.save(department));
  }

  @DeleteMapping("/departments/{id}")
  public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Long id) {
    departmentRepository.deleteById(Math.toIntExact(id));
    return BaseResponseFactory.success(OrganizationStatus.DELETE_DEPARTMENT_SUCCESS);
  }

  @GetMapping("/positions")
  public ResponseEntity<BaseResponse<List<Position>>> getPositions() {
    return BaseResponseFactory.success(
        OrganizationStatus.READ_POSITION_SUCCESS, positionRepository.findAll());
  }

  @PostMapping("/positions")
  public ResponseEntity<BaseResponse<Position>> createPosition(@RequestBody Position position) {
    return BaseResponseFactory.success(
        OrganizationStatus.CREATE_POSITION_SUCCESS, positionRepository.save(position));
  }
}
