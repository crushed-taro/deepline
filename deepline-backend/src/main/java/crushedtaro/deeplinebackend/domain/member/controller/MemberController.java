package crushedtaro.deeplinebackend.domain.member.controller;

import crushedtaro.deeplinebackend.domain.member.dto.ResetPasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.member.dto.FindIdDTO;
import crushedtaro.deeplinebackend.domain.member.dto.MemberDTO;
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
  public ResponseEntity<BaseResponse<Void>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {

    log.info("[MemberController] resetPassword : {}", resetPasswordDTO);

    memberService.resetPassword(resetPasswordDTO);

    return BaseResponseFactory.success(MemberStatus.RESET_PASSWORD_SUCCESS);
  }

  //    @GetMapping("/me")
  //    public ResponseEntity<BaseResponse<MemberResposenDTO>> getMyInfo() {
  //        // SecuriryContextHolder에서 현재 로그인한 유저 정보 꺼내서 조회
  //        return null;
  //    }

  @PutMapping("/me")
  public ResponseEntity<BaseResponse<Void>> updateMyInfo(@RequestBody MemberDTO memberDTO) {
    return null;
  }

  @DeleteMapping("/me")
  public ResponseEntity<BaseResponse<Void>> withdraw() {
    return null;
  }
}
