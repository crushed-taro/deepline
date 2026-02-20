package crushedtaro.deeplinebackend.domain.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.chat.dto.ChatDTO;
import crushedtaro.deeplinebackend.domain.chat.dto.ChatRoomResponse;
import crushedtaro.deeplinebackend.domain.chat.service.ChatService;
import crushedtaro.deeplinebackend.domain.common.BaseResponse;
import crushedtaro.deeplinebackend.domain.common.BaseResponseFactory;
import crushedtaro.deeplinebackend.domain.member.enums.MemberStatus;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

  private final ChatService chatService;

  @GetMapping("/room/{opponentId}")
  public ResponseEntity<BaseResponse<ChatRoomResponse>> getOrCreateRoom(
      @PathVariable String opponentId, Principal principal) {
    log.info("[ChatRestController] getOrCreateRoom : {}", opponentId);
    String myId = principal.getName();
    ChatRoomResponse roomId = chatService.getOrCreatePersonalRoom(myId, opponentId);

    return BaseResponseFactory.success(MemberStatus.READ_LIST_SUCCESS, roomId);
  }

  @GetMapping("/history/{roomId}")
  public ResponseEntity<BaseResponse<List<ChatDTO>>> getChatHistory(@PathVariable Long roomId) {
    log.info("[ChatRestController] getChatHistory : {}", roomId);
    List<ChatDTO> history = chatService.getChatHistory(roomId);
    return BaseResponseFactory.success(MemberStatus.READ_PROFILE_SUCCESS, history);
  }
}
