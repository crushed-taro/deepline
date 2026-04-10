package crushedtaro.deeplinebackend.domain.notification.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
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
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;

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

    Runnable onDisconnect =
        () -> {
          emitters.remove(memberId);
          log.info("[SSE] Client Disconnected : {}", memberId);
          broadcastToOthers("USER_OFFLINE", memberId, memberId);
        };

    emitter.onCompletion(onDisconnect);
    emitter.onTimeout(onDisconnect);
    emitter.onError((e) -> onDisconnect.run());

    try {
      emitter.send(SseEmitter.event().name("connect").data("Connected!"));

      Set<String> onlineUsers = emitters.keySet();

      emitter.send(SseEmitter.event().name("PRESENCE_LIST").data(onlineUsers));
    } catch (IOException e) {
      log.error("[SSE] Connection Error", e);
      emitters.remove(memberId);
    }

    broadcastToOthers("USER_ONLINE", memberId, memberId);

    return emitter;
  }

  @Transactional
  public void saveAndSend(NotificationMessage message) {
    Member receiver =
        memberRepository
            .findByMemberId(message.receiverId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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

  private void broadcastToOthers(String eventName, Object data, String excludeMemberId) {
    emitters.forEach(
        (id, emitter) -> {
          if (!id.equals(excludeMemberId)) {
            try {
              emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
              emitters.remove(id);
            }
          }
        });
  }
}
