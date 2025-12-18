package crushedtaro.deeplinebackend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  boolean existsByMemberEmail(String s);

  Member findByMemberId(String username);

  boolean existsByMemberId(String s);
}
