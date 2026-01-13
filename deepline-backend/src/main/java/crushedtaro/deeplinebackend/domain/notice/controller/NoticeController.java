package crushedtaro.deeplinebackend.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeRequestDTO;
import crushedtaro.deeplinebackend.domain.notice.dto.NoticeResponseDTO;
import crushedtaro.deeplinebackend.domain.notice.enums.NoticeStatus;
import crushedtaro.deeplinebackend.domain.notice.service.NoticeService;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notice API", description = "공지사항 등록 및 조회")
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping
  @Operation(summary = "공지사항 등록", description = "새로운 공지사항을 등록합니다 (관리자/HR 전용).")
  public ResponseEntity<BaseResponse<Void>> createNotice(
      @RequestBody NoticeRequestDTO noticeRequestDTO) {
    log.info("[NoticeController] createNotice : {}", noticeRequestDTO);
    noticeService.createNotice(noticeRequestDTO);
    return BaseResponseFactory.success(NoticeStatus.CREATE_NOTICE_SUCCESS);
  }

  @GetMapping
  @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 페이징하여 조회합니다.")
  public ResponseEntity<BaseResponse<Page<NoticeResponseDTO>>> getNoticesList(
      @PageableDefault(size = 10) Pageable pageable) {
    return BaseResponseFactory.success(
        NoticeStatus.READ_LIST_SUCCESS, noticeService.getNoticeList(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항을 조회하고 조회수를 증가시킵니다.")
  public ResponseEntity<BaseResponse<NoticeResponseDTO>> getNoticeDetail(@PathVariable Long id) {
    return BaseResponseFactory.success(
        NoticeStatus.READ_DETAIL_SUCCESS, noticeService.getNoticeDetail(id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
  public ResponseEntity<BaseResponse<Void>> deleteNotice(@PathVariable Long id) {
    noticeService.deleteNotice(id);
    return BaseResponseFactory.success(NoticeStatus.WITHDRAW_NOTICE_SUCCESS);
  }
}
