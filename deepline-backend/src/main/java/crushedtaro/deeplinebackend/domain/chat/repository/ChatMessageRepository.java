package crushedtaro.deeplinebackend.domain.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.chat.entity.ChatMessage;
import crushedtaro.deeplinebackend.domain.chat.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  @EntityGraph(attributePaths = {"sender"})
  List<ChatMessage> findAllByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);

  @EntityGraph(attributePaths = {"sender"})
  Page<ChatMessage> findAllByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom, Pageable pageable);
}
