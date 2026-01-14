package crushedtaro.deeplinebackend.domain.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
  List<Approval> findAllByMemberOrderByCreatedAtDesc(Member member);

  @Query(
      "select approval from Approval approval "
          + "join approval.approvalLines approvalLine "
          + "join approvalLine.approver member "
          + "where member.memberCode = :memberCode "
          + "and approvalLine.status = 'PENDING' "
          + "order by approval.createdAt desc")
  List<Approval> findWaitApprovals(@Param("memberCode") int memberCode);

  @Query("select a.status, count(a) " + "from Approval a " + "group by a.status ")
  List<Object[]> countApprovalsByStatus();
}
