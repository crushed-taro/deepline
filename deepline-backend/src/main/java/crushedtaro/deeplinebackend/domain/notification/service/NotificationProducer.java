package crushedtaro.deeplinebackend.domain.notification.service;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.notification.dto.NotificationMessage;
import crushedtaro.deeplinebackend.infra.config.RabbitMqConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendNotification(String receiverId, String content, String url) {
    NotificationMessage notificationMessage =
        new NotificationMessage(receiverId, content, url, LocalDateTime.now());

    rabbitTemplate.convertAndSend(
        RabbitMqConfig.EXCHANGE_NAME, "notification.create", notificationMessage);

    log.info("[RabbitMQ] Sent Message to {}: {}", receiverId, content);
  }
}
