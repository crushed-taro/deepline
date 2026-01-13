package crushedtaro.deeplinebackend.domain.notification.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.notification.dto.NotificationMessage;
import crushedtaro.deeplinebackend.domain.notification.entity.Notification;
import crushedtaro.deeplinebackend.domain.notification.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final MemberRepository memberRepository;

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter subscribe(String memberId) {
    SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);

    emitters.put(memberId, emitter);
    log.info("[SSE] New Client Subscribed: {}", memberId);

    emitter.onCompletion(() -> emitters.remove(memberId));
    emitter.onTimeout(() -> emitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    try {
      emitter.send(SseEmitter.event().name("connect").data("Connected!"));
    } catch (IOException e) {
      log.error("[SSE] Connection Error", e);
    }

    return emitter;
  }

  @Transactional
  public void saveAndSend(NotificationMessage message) {
    Member receiver =
        memberRepository
            .findByMemberId(message.receiverId())
            .orElseThrow(() -> new RuntimeException("회원 없음"));

    Notification notification =
        Notification.builder()
            .receiver(receiver)
            .content(message.content())
            .url(message.url())
            .isRead(false)
            .build();

    notificationRepository.save(notification);

    SseEmitter emitter = emitters.get(message.receiverId());
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("notification").data(message));
        log.info("[SSE] Sent to {}", message.receiverId());
      } catch (IOException e) {
        emitters.remove(message.receiverId());
        log.error("[SSE] Send Failed", e);
      }
    }
  }
}
