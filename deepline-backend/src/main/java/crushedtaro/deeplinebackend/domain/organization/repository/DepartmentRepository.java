package crushedtaro.deeplinebackend.domain.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.organization.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {}
