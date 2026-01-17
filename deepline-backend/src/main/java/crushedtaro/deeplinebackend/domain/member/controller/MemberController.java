package crushedtaro.deeplinebackend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.member.dto.*;
import crushedtaro.deeplinebackend.domain.member.enums.MemberStatus;
import crushedtaro.deeplinebackend.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member API", description = "회원 정보 조회, 수정 및 관리자 기능")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/find-id")
  @Operation(summary = "아이디 찾기", description = "이름과 이메일로 아이디를 찾습니다.")
  public ResponseEntity<BaseResponse<String>> findId(@RequestBody FindIdDTO findIdDTO) {

    log.info("[MemberController] findId : {}", findIdDTO);

    String memberId = memberService.findMemberId(findIdDTO);

    return BaseResponseFactory.success(MemberStatus.FIND_ID_SUCCESS, memberId);
  }

  @PostMapping("/reset-password")
  @Operation(summary = "비밀번호 재설정", description = "비밀번호를 분실했을 때 새로운 비밀번호로 변경합니다.")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @RequestBody ResetPasswordDTO resetPasswordDTO) {

    log.info("[MemberController] resetPassword : {}", resetPasswordDTO);

    memberService.resetPassword(resetPasswordDTO);

    return BaseResponseFactory.success(MemberStatus.RESET_PASSWORD_SUCCESS);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 상세 정보를 조회합니다.")
  public ResponseEntity<BaseResponse<MemberResponseDTO>> getMyInfo() {

    log.info("[MemberController] getMyInfo");

    MemberResponseDTO memberResponseDTO = memberService.getMyInfo();

    return BaseResponseFactory.success(MemberStatus.READ_PROFILE_SUCCESS, memberResponseDTO);
  }

  @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "내 정보 수정", description = "이메일 등 내 정보를 수정합니다.")
  public ResponseEntity<BaseResponse<Void>> updateMyInfo(
      @RequestPart(value = "data") UpdateMemberDTO updateMemberDTO,
      @RequestPart(value = "image", required = false) MultipartFile multipartFile) {

    log.info("[MemberController] updateMyInfo : {}", updateMemberDTO);

    memberService.updateMyInfo(updateMemberDTO, multipartFile);

    return BaseResponseFactory.success(MemberStatus.UPDATE_INFO_SUCCESS);
  }

  @DeleteMapping("/me")
  @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다 (논리적 삭제).")
  public ResponseEntity<BaseResponse<Void>> withdraw() {

    log.info("[MemberController] withdraw");

    memberService.withdraw();

    return BaseResponseFactory.success(MemberStatus.WITHDRAW_SUCCESS);
  }

  @PutMapping("/{memberId}/assign")
  @PreAuthorize("hasRole('HR')")
  @Operation(summary = "인사 발령 (관리자)", description = "특정 사원의 부서와 직급을 변경합니다.")
  public ResponseEntity<BaseResponse<Void>> assignMember(
      @PathVariable String memberId, @RequestBody MemberAssignmentDTO assignmentDTO) {

    log.info("[MemberController] assignMember : Target={}, DTO={}", memberId, assignmentDTO);

    memberService.assignMember(memberId, assignmentDTO);

    return BaseResponseFactory.success(MemberStatus.ASSIGN_SUCCESS);
  }

  @GetMapping
  @Operation(summary = "회원 목록 조회", description = "전체 회원 목록을 페이징하여 조회합니다.")
  public ResponseEntity<BaseResponse<Page<MemberResponseDTO>>> getMemberList(
      @PageableDefault(size = 10, sort = "memberCode", direction = Sort.Direction.DESC)
          Pageable pageable,
      @RequestParam(required = false) String searchName) {
    log.info("[MemberController] getMemberList: searchName={}", searchName);
    Page<MemberResponseDTO> memberList = memberService.getMemberList(pageable, searchName);

    return BaseResponseFactory.success(MemberStatus.READ_LIST_SUCCESS, memberList);
  }
}
