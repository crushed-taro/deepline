package crushedtaro.deeplinebackend.domain.member.repository;

import org.springframework.data.repository.CrudRepository;

import crushedtaro.deeplinebackend.domain.member.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {}
