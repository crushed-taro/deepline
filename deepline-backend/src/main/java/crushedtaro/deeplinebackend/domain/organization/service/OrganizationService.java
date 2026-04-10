package crushedtaro.deeplinebackend.domain.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.organization.dto.DepartmentDTO;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

  private final DepartmentRepository departmentRepository;

  @Transactional(readOnly = true)
  @Cacheable(value = "departments", key = "'all'")
  public List<DepartmentDTO> getAllDepartments() {

    log.info("[Cache Miss] DB에서 부서 목록을 직접 조회합니다.");

    return departmentRepository.findAll().stream()
        .map(dept -> new DepartmentDTO(dept.getDeptCode(), dept.getDeptName()))
        .collect(Collectors.toList());
  }

  @Transactional
  @CacheEvict(value = "departments", key = "'all'")
  public Department createDepartment(Department departmentDTO) {
    log.info("[Cache Evict] 새로운 부서가 추가되어 기존 부서 캐시를 초기화합니다.");
    return departmentRepository.save(departmentDTO);
  }

  @Transactional
  @CacheEvict(value = "departments", key = "'all'")
  public void deleteDepartment(Long id) {
    log.info("[Cache Evict] 부서가 삭제되어 기존 부서 캐시를 초기화합니다.");
    departmentRepository.deleteById(Math.toIntExact(id));
  }
}
