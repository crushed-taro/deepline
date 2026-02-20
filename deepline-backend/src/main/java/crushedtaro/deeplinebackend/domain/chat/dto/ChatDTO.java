package crushedtaro.deeplinebackend.domain.chat.dto;

public record ChatDTO(
    String roomId,
    String senderId,
    String senderName,
    String message,
    String timestamp,
    String receiverId) {}
