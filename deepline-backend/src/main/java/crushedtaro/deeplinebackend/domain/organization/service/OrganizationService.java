package crushedtaro.deeplinebackend.domain.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.organization.dto.DepartmentDTO;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrganizationService {

  private final DepartmentRepository departmentRepository;

  @Cacheable(value = "departments", key = "'all'")
  public List<DepartmentDTO> getAllDepartments() {

    log.info("[Cache Miss] DB에서 부서 목록을 직접 조회합니다.");

    return departmentRepository.findAll().stream()
        .map(dept -> new DepartmentDTO(dept.getDeptCode(), dept.getDeptName()))
        .collect(Collectors.toList());
  }
}
