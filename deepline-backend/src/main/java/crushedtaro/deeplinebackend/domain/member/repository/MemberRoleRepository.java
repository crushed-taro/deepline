package crushedtaro.deeplinebackend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.member.entity.MemberRole;
import crushedtaro.deeplinebackend.domain.member.entity.MemberRolePk;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRolePk> {}
