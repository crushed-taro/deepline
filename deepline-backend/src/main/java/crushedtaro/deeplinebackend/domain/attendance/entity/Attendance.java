package crushedtaro.deeplinebackend.domain.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.attendance.enums.AttendanceStatus;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_attendance")
public class Attendance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "attendance_code")
  private Long attendanceCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_code", nullable = false)
  private Member member;

  @Column(name = "work_date", nullable = false)
  private LocalDate workDate;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private AttendanceStatus status;

  public void clockOut(LocalDateTime now) {
    this.endTime = now;
  }
}
