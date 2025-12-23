package crushedtaro.deeplinebackend.domain.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
  List<Approval> findAllByMemberOrderByCreatedAtDesc(Member member);
}
