package crushedtaro.deeplinebackend.domain.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.approval.entity.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {}
