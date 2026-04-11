package crushedtaro.deeplinebackend.domain.approval.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
  @EntityGraph(attributePaths = {"member"})
  List<Approval> findAllByMemberOrderByCreatedAtDesc(Member member);

  @EntityGraph(attributePaths = {"member", "approvalLines", "approvalLines.approver"})
  @Query(
      "select distinct approval from Approval approval "
          + "join approval.approvalLines approvalLine "
          + "join approvalLine.approver member "
          + "where member.memberCode = :memberCode "
          + "and approvalLine.status = 'PENDING' "
          + "order by approval.createdAt desc")
  List<Approval> findWaitApprovals(@Param("memberCode") int memberCode);

  @EntityGraph(
      attributePaths = {
        "member",
        "member.department",
        "approvalLines",
        "approvalLines.approver",
        "approvalLines.approver.position"
      })
  Optional<Approval> findDetailByApprovalCode(Long approvalCode);

  @Query("select a.status, count(a) " + "from Approval a " + "group by a.status ")
  List<Object[]> countApprovalsByStatus();
}
