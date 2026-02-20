package crushedtaro.deeplinebackend.domain.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import crushedtaro.deeplinebackend.domain.organization.dto.DepartmentDTO;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationService {

  private final DepartmentRepository departmentRepository;

  public List<DepartmentDTO> getAllDepartments() {
    return departmentRepository.findAll().stream()
        .map(dept -> new DepartmentDTO(dept.getDeptCode(), dept.getDeptName()))
        .collect(Collectors.toList());
  }
}
