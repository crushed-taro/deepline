package crushedtaro.deeplinebackend.domain.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.organization.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {}
