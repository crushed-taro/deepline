package crushedtaro.deeplinebackend.domain.approval.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalDetailDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalListDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalProcessDTO;
import crushedtaro.deeplinebackend.domain.approval.dto.ApprovalRegistDTO;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalResponseStatus;
import crushedtaro.deeplinebackend.domain.approval.service.ApprovalService;
import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Approval API", description = "전자결재 상신, 조회 및 승인/반려 처리")
public class ApprovalController {

  private final ApprovalService approvalService;

  @PostMapping
  @Operation(summary = "결재 상신", description = "새로운 결재 문서를 작성하여 상신합니다.")
  public ResponseEntity<BaseResponse<Void>> registerApproval(
      @RequestBody ApprovalRegistDTO approvalRegistDTO) {

    log.info("[ApprovalController] registerApproval Start");

    approvalService.registerApproval(approvalRegistDTO);

    return BaseResponseFactory.success(ApprovalResponseStatus.APPROVAL_REGIST_SUCCESS);
  }

  @GetMapping("/sent")
  @Operation(summary = "기안함 조회", description = "내가 상신한 결재 문서 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<ApprovalListDTO>>> getSentApprovals() {
    log.info("[ApprovalController] getSentApproval Start");
    List<ApprovalListDTO> approvalList = approvalService.getSentApprovals();
    return BaseResponseFactory.success(ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalList);
  }

  @GetMapping("/received")
  @Operation(summary = "결재함 조회", description = "내가 결재해야 할 문서 목록(대기중)을 조회합니다.")
  public ResponseEntity<BaseResponse<List<ApprovalListDTO>>> getReceivedApprovals() {
    log.info("[ApprovalController] getReceivedApproval Start");

    List<ApprovalListDTO> approvalList = approvalService.getReceivedApprovals();
    return BaseResponseFactory.success(ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalList);
  }

  @GetMapping("/{approvalCode}")
  @Operation(summary = "결재 상세 조회", description = "특정 결재 문서의 상세 내용과 결재선을 조회합니다.")
  public ResponseEntity<BaseResponse<ApprovalDetailDTO>> getApprovalDetail(
      @PathVariable Long approvalCode) {
    log.info("[ApprovalController] getApprovalDetail Start");

    ApprovalDetailDTO approvalDetailDTO = approvalService.getApprovalDetail(approvalCode);
    return BaseResponseFactory.success(
        ApprovalResponseStatus.READ_PROFILE_SUCCESS, approvalDetailDTO);
  }

  @PostMapping("/{approvalCode}/process")
  @Operation(summary = "결재 처리 (승인/반려)", description = "내 차례인 문서를 승인하거나 반려합니다.")
  public ResponseEntity<BaseResponse<Void>> processApproval(
      @PathVariable Long approvalCode, @RequestBody ApprovalProcessDTO approvalProcessDTO) {
    log.info("[ApprovalController] processApproval Start");

    approvalService.processApproval(approvalCode, approvalProcessDTO);

    return BaseResponseFactory.success(ApprovalResponseStatus.APPROVAL_PROCESS_SUCCESS);
  }
}
