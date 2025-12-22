package crushedtaro.deeplinebackend.domain.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.attendance.entity.Attendance;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

  Optional<Attendance> findByMemberAndWorkDate(Member member, LocalDate workDate);

  boolean existsByMemberAndWorkDate(Member member, LocalDate workDate);
}
