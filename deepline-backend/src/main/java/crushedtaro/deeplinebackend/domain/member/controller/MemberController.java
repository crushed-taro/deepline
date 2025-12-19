package crushedtaro.deeplinebackend.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/find-id")
  public ResponseEntity<BaseResponse<String>> findId(@RequestBody FindIdDTO findIdDTO) {

    log.info("[MemberController] findId : {}", findIdDTO);

    String memberId = memberService.findMemberId(findIdDTO);

    return BaseResponseFactory.success(MemberStatus.FIND_ID_SUCCESS, memberId);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @RequestBody ResetPasswordDTO resetPasswordDTO) {

    log.info("[MemberController] resetPassword : {}", resetPasswordDTO);

    memberService.resetPassword(resetPasswordDTO);

    return BaseResponseFactory.success(MemberStatus.RESET_PASSWORD_SUCCESS);
  }

  @GetMapping("/me")
  public ResponseEntity<BaseResponse<MemberResponseDTO>> getMyInfo() {

    log.info("[MemberController] getMyInfo");

    MemberResponseDTO memberResponseDTO = memberService.getMyInfo();

    return BaseResponseFactory.success(MemberStatus.READ_PROFILE_SUCCESS, memberResponseDTO);
  }

  @PutMapping("/me")
  public ResponseEntity<BaseResponse<Void>> updateMyInfo(
      @RequestBody UpdateMemberDTO updateMemberDTO) {

    log.info("[MemberController] updateMyInfo : {}", updateMemberDTO);

    memberService.updateMyInfo(updateMemberDTO);

    return BaseResponseFactory.success(MemberStatus.UPDATE_INFO_SUCCESS);
  }

  @DeleteMapping("/me")
  public ResponseEntity<BaseResponse<Void>> withdraw() {

    log.info("[MemberController] withdraw");

    memberService.withdraw();

    return BaseResponseFactory.success(MemberStatus.WITHDRAW_SUCCESS);
  }
}
