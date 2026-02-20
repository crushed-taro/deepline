package crushedtaro.deeplinebackend.domain.member.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.*;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;
import crushedtaro.deeplinebackend.domain.organization.repository.PositionRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;

  @Transactional(readOnly = true)
  public String findMemberId(FindIdDTO findIdDTO) {
    log.info("[MemberService] findMemberId() START");

    Member member =
        memberRepository
            .findByMemberNameAndMemberEmail(findIdDTO.memberName(), findIdDTO.memberEmail())
            .orElseThrow(() -> new RuntimeException("일치하는 회원 정보가 없습니다."));

    log.info("[MemberService] findMemberid() END");

    return member.getMemberId();
  }

  @Transactional
  public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
    log.info("[MemberService] resetPassword() START");

    Member member =
        memberRepository
            .findByMemberIdAndMemberNameAndMemberEmail(
                resetPasswordDTO.memberId(),
                resetPasswordDTO.memberName(),
                resetPasswordDTO.memberEmail())
            .orElseThrow(() -> new RuntimeException("일치하는 회원 정보가 없습니다."));

    if (!resetPasswordDTO.isPasswordMatched()) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String encodedPassword = passwordEncoder.encode(resetPasswordDTO.newPassword());
    log.debug("[MemberService] Generated Temp Password : {}", encodedPassword);

    member.changePassword(encodedPassword);

    log.info("[MemberService] resetPassword() END");
    return encodedPassword;
  }

  @Transactional(readOnly = true)
  public MemberResponseDTO getMyInfo() {
    log.info("[MemberService] getMyInfo() START");

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
      throw new RuntimeException("인증 정보가 없습니다.");
    }

    String memberId = authentication.getName();
    log.info("[MemberService] Current User ID : {}", memberId);

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    String deptName =
        (member.getDepartment() != null) ? member.getDepartment().getDeptName() : "소속 없음";

    String positionName =
        (member.getPosition() != null) ? member.getPosition().getPositionName() : "직급 없음";

    log.info("[MemberService] getMyInfo() END");

    return new MemberResponseDTO(
        member.getMemberCode(),
        member.getMemberId(),
        member.getMemberName(),
        member.getMemberEmail(),
        member.getRemainVacation(),
        member.getCreatedAt(),
        deptName,
        positionName,
        member.getProfileUrl());
  }

  @Transactional
  public void withdraw() {
    log.info("[MemberService] withdraw() START");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    log.info("[MemberService] Current User ID : {}", memberId);

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    member.withdraw();

    log.info("[MemberService] withdraw() END");
  }

  @Transactional
  public void updateMyInfo(UpdateMemberDTO updateMemberDTO, MultipartFile imageFile) {
    log.info("[MemberService] updateMyInfo() START");

    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보를 찾을 수 없습니다."));

    String newEmail = updateMemberDTO.memberEmail();
    if (newEmail != null && !newEmail.equals(member.getMemberEmail())) {
      if (memberRepository.existsByMemberEmail(newEmail)) {
        throw new RuntimeException("이미 사용 중인 이메일입니다.");
      }
    }

    member.updateMemberInfo(updateMemberDTO.memberName(), newEmail);

    if (imageFile != null && !imageFile.isEmpty()) {
      try {

        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
          Files.createDirectories(uploadPath);
        }

        String oldProfileUrl = member.getProfileUrl();

        String originalFilename = imageFile.getOriginalFilename();
        String storeFileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(storeFileName);

        Files.copy(imageFile.getInputStream(), filePath);

        String fileUrl = "/images/" + storeFileName;
        member.updateProfileUrl(fileUrl);

        if (oldProfileUrl != null && oldProfileUrl.startsWith("/images/")) {
          String oldFileName = oldProfileUrl.replace("/images/", "");
          Path oldFilePath = uploadPath.resolve(oldFileName);

          try {
            Files.deleteIfExists(oldFilePath);
            log.info("[MemberService] 기존 프로필 이미지 삭제 완료: {}", oldFileName);
          } catch (IOException e) {
            log.warn("[MemberService] 기존 프로필 이미지 삭제 실패: {}", oldFileName);
          }
        }

      } catch (IOException e) {
        throw new RuntimeException("프로필 이미지 저장 중 오류가 발생했습니다.", e);
      }
    }

    log.info("[MemberService] updateMyInfo() END");
  }

  @Transactional
  public void assignMember(String memberId, MemberAssignmentDTO memberAssignmentDTO) {
    log.info("[MemberService] assignMember() START");

    Member member =
        memberRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

    Department department =
        departmentRepository
            .findById(memberAssignmentDTO.deptCode())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 부서입니다."));

    Position position =
        positionRepository
            .findById(memberAssignmentDTO.positionCode())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 직급입니다."));

    member.assignDepartment(department);
    member.assignPosition(position);

    log.info("[MemberService] assignMember() END");
  }

  @Transactional(readOnly = true)
  public Page<MemberResponseDTO> getMemberList(Pageable pageable, String searchName) {
    log.info("[MemberService] getMemberList() START");

    Page<Member> memberPage;

    if (searchName != null && !searchName.isEmpty()) {
      memberPage = memberRepository.findByMemberNameContaining(searchName, pageable);
    } else {
      memberPage = memberRepository.findAll(pageable);
    }

    Page<MemberResponseDTO> memberResponseDTOPage =
        memberPage.map(
            member -> {
              String deptName =
                  (member.getDepartment() != null) ? member.getDepartment().getDeptName() : "미배정";
              String positionName =
                  (member.getPosition() != null) ? member.getPosition().getPositionName() : "미배정";

              return new MemberResponseDTO(
                  member.getMemberCode(),
                  member.getMemberId(),
                  member.getMemberName(),
                  member.getMemberEmail(),
                  member.getRemainVacation(),
                  member.getCreatedAt(),
                  deptName,
                  positionName,
                  member.getProfileUrl());
            });

    log.info("[MemberService] getMemberList() END");

    return memberResponseDTOPage;
  }

  @Transactional(readOnly = true)
  public List<MemberResponseDTO> findMembersByDepartment(int departmentCode) {
    return memberRepository.findAllByDepartment_DeptCode(departmentCode).stream()
        .map(MemberResponseDTO::from)
        .collect(Collectors.toList());
  }
}
