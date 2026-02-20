package crushedtaro.deeplinebackend.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByRoomId(String roomId);

  Optional<ChatRoom> findByChatRoomCode(Long roomId);
}
