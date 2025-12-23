package crushedtaro.deeplinebackend.domain.approval.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalRegistDTO;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalResponseStatus;
import crushedtaro.deeplinebackend.domain.approval.service.ApprovalService;
import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
@Slf4j
public class ApprovalController {

  private final ApprovalService approvalService;

  @PostMapping
  public ResponseEntity<BaseResponse<Void>> registerApproval(
      @RequestBody ApprovalRegistDTO approvalRegistDTO) {

    log.info("[ApprovalController] registerApproval Start");

    approvalService.registerApproval(approvalRegistDTO);

    return BaseResponseFactory.success(ApprovalResponseStatus.APPROVAL_REGIST_SUCCESS);
  }
}
