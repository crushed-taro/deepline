package crushedtaro.deeplinebackend.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.chat.dto.ChatDTO;
import crushedtaro.deeplinebackend.domain.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final SimpMessageSendingOperations messagingTemplate;
  private final ChatService chatService;

  @MessageMapping("/chat.sendMessage")
  public void sendMessage(@Payload ChatDTO chatDTO) {
    ChatDTO savedChat = chatService.saveMessage(chatDTO);

    log.info("[ChatController] sendMessage : {}", chatDTO);

    messagingTemplate.convertAndSend("/topic/chat." + chatDTO.roomId(), savedChat);
    messagingTemplate.convertAndSend("/topic/chat.user." + chatDTO.receiverId(), chatDTO);
  }
}
