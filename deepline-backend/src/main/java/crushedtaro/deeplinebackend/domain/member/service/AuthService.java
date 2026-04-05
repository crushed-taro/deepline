package crushedtaro.deeplinebackend.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.MemberDTO;
import crushedtaro.deeplinebackend.domain.member.dto.SignupResponseDTO;
import crushedtaro.deeplinebackend.domain.member.dto.TokenDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.entity.MemberRole;
import crushedtaro.deeplinebackend.domain.member.enums.SignupStatus;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRoleRepository;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;
import crushedtaro.deeplinebackend.domain.organization.repository.PositionRepository;
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;
import crushedtaro.deeplinebackend.infra.jwt.TokenProvider;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final MemberRoleRepository memberRoleRepository;
  private final TokenProvider tokenProvider;
  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;

  @Transactional
  public SignupResponseDTO signup(MemberDTO member) {
    log.info("[AuthService] signup() START");
    log.info("[AuthService] member : {}", member);

    if (memberRepository.existsByMemberEmail(member.memberEmail())) {
      log.warn("[AuthService] signup() Duplicated Email Found!");
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    String encodedPassword = passwordEncoder.encode(member.memberPassword());

    Department defaultDept = departmentRepository.findById(1).orElse(null);
    Position defaultPos = positionRepository.findById(1).orElse(null);

    Member registMember =
        Member.builder()
            .memberId(member.memberId())
            .memberPassword(encodedPassword)
            .memberName(member.memberName())
            .memberEmail(member.memberEmail())
            .department(defaultDept)
            .position(defaultPos)
            .build();

    Member savedMember = memberRepository.save(registMember);

    int memberCode = savedMember.getMemberCode();

    MemberRole newRole = MemberRole.builder().memberNo(memberCode).authorityCode(2).build();

    memberRoleRepository.save(newRole);

    log.info("[AuthService] signup() END");

    return new SignupResponseDTO(
        memberCode, SignupStatus.SIGNUP_SUCCESS, savedMember.getMemberEmail());
  }

  @Transactional
  public TokenDTO login(MemberDTO member) {
    log.info("[AuthService] login() START");
    log.info("[AuthService] member : {}", member);

    Member registedUser =
        memberRepository
            .findByMemberId(member.memberId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!memberRepository.existsByMemberId(member.memberId())) {
      log.warn("[AuthService] Login() Required User Not Found!");
      throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
    }

    if ("Y".equals(registedUser.getIsDeleted())) {
      log.warn("[AuthService] Login() Deleted User Attempted Login!");
      throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
    }

    if (!passwordEncoder.matches(member.memberPassword(), registedUser.getMemberPassword())) {
      log.warn("[AuthService] Login() Password Match Failed!");
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    TokenDTO newTokenDTO = tokenProvider.generateTokenDTO(registedUser);

    return newTokenDTO;
  }
}
