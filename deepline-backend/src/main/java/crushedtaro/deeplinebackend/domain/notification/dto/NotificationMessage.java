package crushedtaro.deeplinebackend.domain.notification.dto;

import java.time.LocalDateTime;

public record NotificationMessage(
    String receiverId, String content, String url, LocalDateTime timestamp) {}
