package crushedtaro.deeplinebackend.domain.notification.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.notification.dto.NotificationMessage;
import crushedtaro.deeplinebackend.infra.config.RabbitMqConfig;

@Service
@Slf4j
public class NotificationConsumer {

  @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
  public void receiveMessage(NotificationMessage notificationMessage) {
    log.info("=================================================");
    log.info("[RabbitMQ] Received Message!");
    log.info("To: {}", notificationMessage.receiverId());
    log.info("Content: {}", notificationMessage.content());
    log.info("Link: {}", notificationMessage.url());
    log.info("=================================================");
  }
}
