package crushedtaro.deeplinebackend.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  boolean existsByMemberEmail(String s);

  Optional<Member> findByMemberId(String username);

  boolean existsByMemberId(String s);

  Optional<Member> findByMemberNameAndMemberEmail(String memberName, String memberEmail);

  Optional<Member> findByMemberIdAndMemberNameAndMemberEmail(
      String memberId, String memberName, String memberEmail);

  @EntityGraph(attributePaths = {"department", "position"})
  Page<Member> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"department", "position"})
  Page<Member> findByMemberNameContaining(String memberName, Pageable pageable);

  List<Member> findAllByDepartment_DeptCode(int departmentCode);
}
