package crushedtaro.deeplinebackend.domain.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.attendance.dto.AttendanceResponseDTO;
import crushedtaro.deeplinebackend.domain.attendance.entity.Attendance;
import crushedtaro.deeplinebackend.domain.attendance.enums.AttendanceStatus;
import crushedtaro.deeplinebackend.domain.attendance.repository.AttendanceRepository;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceService {

  private final AttendanceRepository attendanceRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void clockIn() {
    log.info("[AttendanceService] ClockIn Start");
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    LocalDate today = LocalDate.now();

    if (attendanceRepository.existsByMemberAndWorkDate(member, today)) {
      throw new RuntimeException("이미 출근 처리가 되었습니다");
    }

    LocalDateTime now = LocalDateTime.now();
    LocalTime workStartTime = LocalTime.of(9, 0);

    AttendanceStatus status = AttendanceStatus.PRESENT;
    if (now.toLocalTime().isAfter(workStartTime)) {
      status = AttendanceStatus.LATE;
    }

    Attendance attendance =
        Attendance.builder().member(member).workDate(today).startTime(now).status(status).build();

    attendanceRepository.save(attendance);
    log.info("[Attendance] Clock-In End");
  }

  @Transactional
  public void clockOut() {
    log.info("[AttendanceService] ClockOut Start");
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    LocalDate today = LocalDate.now();

    Attendance attendance =
        attendanceRepository
            .findByMemberAndWorkDate(member, today)
            .orElseThrow(() -> new RuntimeException("출근 기록이 없습니다."));

    attendance.clockOut(LocalDateTime.now());

    log.info("[Attendance] Clock-out End");
  }

  @Transactional(readOnly = true)
  public List<AttendanceResponseDTO> getMyAttendance(int year, int month) {
    log.info("[AttendanceService] getMyAttendance Start");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

    List<Attendance> attendanceList =
        attendanceRepository.findAllByMemberAndWorkDateBetweenOrderByWorkDateAsc(
            member, startDate, endDate);

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    return attendanceList.stream()
        .map(
            attendance ->
                new AttendanceResponseDTO(
                    attendance.getWorkDate(),
                    (attendance.getStartTime() != null)
                        ? attendance.getStartTime().format(timeFormatter)
                        : null,
                    (attendance.getEndTime() != null)
                        ? attendance.getEndTime().format(timeFormatter)
                        : null,
                    attendance.getStatus()))
        .collect(Collectors.toList());
  }
}
