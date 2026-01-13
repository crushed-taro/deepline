package crushedtaro.deeplinebackend.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.notification.service.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification API", description = "실시간 알림 (SSE)")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(summary = "알림 구독 (SSE 연결)", description = "로그인한 사용자가 실시간 알림을 받기 위해 서버와 연결합니다.")
  public SseEmitter subscribe(@AuthenticationPrincipal UserDetails userDetails) {
    String memberId = userDetails.getUsername();
    log.info("[NotificationController] Subscribe: {}", memberId);
    return notificationService.subscribe(memberId);
  }
}
