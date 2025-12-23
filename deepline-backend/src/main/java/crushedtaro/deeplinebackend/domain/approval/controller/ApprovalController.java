package crushedtaro.deeplinebackend.domain.approval.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalDetailDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalListDTO;
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

  @GetMapping("/sent")
  public ResponseEntity<BaseResponse<List<ApprovalListDTO>>> getSentApprovals() {
    log.info("[ApprovalController] getSentApproval Start");
    List<ApprovalListDTO> approvalList = approvalService.getSentApprovals();
    return BaseResponseFactory.success(ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalList);
  }

  @GetMapping("/received")
  public ResponseEntity<BaseResponse<List<ApprovalListDTO>>> getReceivedApprovals() {
    log.info("[ApprovalController] getReceivedApproval Start");

    List<ApprovalListDTO> approvalList = approvalService.getReceivedApprovals();
    return BaseResponseFactory.success(ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalList);
  }

  @GetMapping("/{approvalCode}")
  public ResponseEntity<BaseResponse<ApprovalDetailDTO>> getApprovalDetail(
      @PathVariable Long approvalCode) {
    log.info("[ApprovalController] getApprovalDetail Start");

    ApprovalDetailDTO approvalDetailDTO = approvalService.getApprovalDetail(approvalCode);
    return BaseResponseFactory.success(
        ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalDetailDTO);
  }
}
