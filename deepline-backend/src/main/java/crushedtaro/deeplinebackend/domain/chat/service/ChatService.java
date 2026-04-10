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
import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;

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
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

    Member sender =
        memberRepository
            .findByMemberId(chatDTO.senderId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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
            .orElseThrow(() -> new CustomException(ErrorCode.SENDER_NOT_FOUND));
    Member receiver =
        memberRepository
            .findByMemberId(receiverId)
            .orElseThrow(() -> new CustomException(ErrorCode.RECEIVER_NOT_FOUND));

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
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

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
