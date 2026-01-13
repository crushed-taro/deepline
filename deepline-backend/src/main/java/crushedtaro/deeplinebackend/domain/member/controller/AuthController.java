package crushedtaro.deeplinebackend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.member.dto.MemberDTO;
import crushedtaro.deeplinebackend.domain.member.dto.SignupResponseDTO;
import crushedtaro.deeplinebackend.domain.member.dto.TokenDTO;
import crushedtaro.deeplinebackend.domain.member.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth API", description = "회원가입 및 로그인, 토큰 관리")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
  public ResponseEntity<BaseResponse<SignupResponseDTO>> signup(@RequestBody MemberDTO member) {

    log.info("[AuthController] signup member: {}", member);

    SignupResponseDTO signupResponseDTO = authService.signup(member);

    log.info("[AuthController] signup End");

    return BaseResponseFactory.create(signupResponseDTO.signupStatus(), signupResponseDTO);
  }

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "ID/PW로 로그인하여 JWT 토큰을 발급받습니다.")
  public ResponseEntity<BaseResponse<TokenDTO>> login(@RequestBody MemberDTO memberDTO) {
    log.info("[AuthController] login member: {}", memberDTO);

    TokenDTO loginResponseDTO = authService.login(memberDTO);

    log.info("[AuthController] login End");

    return BaseResponseFactory.success(loginResponseDTO.tokenStatus(), loginResponseDTO);
  }
}
