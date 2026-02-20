package crushedtaro.deeplinebackend.domain.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.chat.dto.ChatDTO;
import crushedtaro.deeplinebackend.domain.chat.dto.ChatRoomResponse;
import crushedtaro.deeplinebackend.domain.chat.entity.ChatMessage;
import crushedtaro.deeplinebackend.domain.chat.entity.ChatRoom;
import crushedtaro.deeplinebackend.domain.chat.repository.ChatMessageRepository;
import crushedtaro.deeplinebackend.domain.chat.repository.ChatRoomRepository;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final MemberRepository memberRepository;

  public ChatDTO saveMessage(ChatDTO chatDTO) {
    ChatRoom room =
        chatRoomRepository
            .findByRoomId(chatDTO.roomId())
            .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

    Member sender =
        memberRepository
            .findByMemberId(chatDTO.senderId())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    ChatMessage chatMessage =
        ChatMessage.builder().chatRoom(room).sender(sender).message(chatDTO.message()).build();

    chatMessageRepository.save(chatMessage);

    return new ChatDTO(
        room.getRoomId(),
        sender.getMemberId(),
        sender.getMemberName(),
        chatMessage.getMessage(),
        chatMessage.getCreatedAt() != null ? chatMessage.getCreatedAt().toString() : "",
        chatDTO.receiverId());
  }

  public ChatRoomResponse getOrCreatePersonalRoom(String senderId, String receiverId) {
    Member sender =
        memberRepository
            .findByMemberId(senderId)
            .orElseThrow(() -> new RuntimeException("발신자 정보가 없습니다."));
    Member receiver =
        memberRepository
            .findByMemberId(receiverId)
            .orElseThrow(() -> new RuntimeException("수신자 정보가 없습니다."));

    String roomId =
        (senderId.compareTo(receiverId) < 0)
            ? senderId + "_" + receiverId
            : receiverId + "_" + senderId;

    ChatRoom room =
        chatRoomRepository
            .findByRoomId(roomId)
            .orElseGet(
                () -> {
                  ChatRoom newRoom =
                      ChatRoom.builder()
                          .roomId(roomId)
                          .roomName(
                              sender.getMemberName() + ", " + receiver.getMemberName() + "의 대화")
                          .type(ChatRoom.ChatType.PERSONAL)
                          .member1(sender)
                          .member2(receiver)
                          .build();
                  return chatRoomRepository.save(newRoom);
                });

    return new ChatRoomResponse(room.getChatRoomCode(), room.getRoomId());
  }

  @Transactional(readOnly = true)
  public List<ChatDTO> getChatHistory(Long roomId) {
    log.info("[ChatService] getChatHistory : {}", roomId);

    ChatRoom room =
        chatRoomRepository
            .findByChatRoomCode(roomId)
            .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

    return chatMessageRepository.findAllByChatRoomOrderByCreatedAtAsc(room).stream()
        .map(
            msg -> {
              String receiverId = "";
              if (room.getType() == ChatRoom.ChatType.PERSONAL) {
                receiverId =
                    msg.getSender().getMemberId().equals(room.getMember1().getMemberId())
                        ? room.getMember2().getMemberId()
                        : room.getMember1().getMemberId();
              }

              return new ChatDTO(
                  room.getRoomId(),
                  msg.getSender().getMemberId(),
                  msg.getSender().getMemberName(),
                  msg.getMessage(),
                  msg.getCreatedAt().toString(),
                  receiverId);
            })
        .collect(Collectors.toList());
  }
}
