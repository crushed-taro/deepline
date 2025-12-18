package crushedtaro.deeplinebackend.domain.member.controller;

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
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<SignupResponseDTO>> signup(@RequestBody MemberDTO member) {

    log.info("[AuthController] signup member: {}", member);

    SignupResponseDTO signupResponseDTO = authService.signup(member);

    return BaseResponseFactory.create(signupResponseDTO.signupStatus(), signupResponseDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<TokenDTO>> login(@RequestBody MemberDTO memberDTO) {
    log.info("[AuthController] login member: {}", memberDTO);

    TokenDTO loginResponseDTO = authService.login(memberDTO);

    return BaseResponseFactory.success(loginResponseDTO.tokenStatus(), loginResponseDTO);
  }
}
