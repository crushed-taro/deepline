package crushedtaro.deeplinebackend.domain.notice.controller;

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
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping
  public ResponseEntity<BaseResponse<Void>> createNotice(
      @RequestBody NoticeRequestDTO noticeRequestDTO) {
    log.info("[NoticeController] createNotice : {}", noticeRequestDTO);
    noticeService.createNotice(noticeRequestDTO);
    return BaseResponseFactory.success(NoticeStatus.CREATE_NOTICE_SUCCESS);
  }

  @GetMapping
  public ResponseEntity<BaseResponse<Page<NoticeResponseDTO>>> getNoticesList(
      @PageableDefault(size = 10) Pageable pageable) {
    return BaseResponseFactory.success(
        NoticeStatus.READ_LIST_SUCCESS, noticeService.getNoticeList(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<NoticeResponseDTO>> getNoticeDetail(@PathVariable Long id) {
    return BaseResponseFactory.success(
        NoticeStatus.READ_DETAIL_SUCCESS, noticeService.getNoticeDetail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BaseResponse<Void>> deleteNotice(@PathVariable Long id) {
    noticeService.deleteNotice(id);
    return BaseResponseFactory.success(NoticeStatus.WITHDRAW_NOTICE_SUCCESS);
  }
}
