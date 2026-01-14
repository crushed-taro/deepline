package crushedtaro.deeplinebackend.domain.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import crushedtaro.deeplinebackend.domain.attendance.entity.Attendance;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

  Optional<Attendance> findByMemberAndWorkDate(Member member, LocalDate workDate);

  boolean existsByMemberAndWorkDate(Member member, LocalDate workDate);

  List<Attendance> findAllByMemberAndWorkDateBetweenOrderByWorkDateAsc(
      Member member, LocalDate startDate, LocalDate endDate);

  @Query(
      "SELECT a.workDate, a.status, COUNT(a) "
          + "FROM Attendance a "
          + "WHERE year(a.workDate) = :year "
          + "AND month(a.workDate) = :month "
          + "GROUP BY a.workDate, a.status "
          + "ORDER BY a.workDate ASC")
  List<Object[]> findDailyStatistics(@Param("year") int year, @Param("month") int month);
}
