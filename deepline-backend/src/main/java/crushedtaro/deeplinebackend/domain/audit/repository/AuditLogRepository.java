package crushedtaro.deeplinebackend.domain.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.audit.entity.Audit;

public interface AuditLogRepository extends JpaRepository<Audit, Long> {}
