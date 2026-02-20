package crushedtaro.deeplinebackend.domain.chat.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Table(name = "tbl_chat_message")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_code")
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_code")
  private Member sender;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String message;

  @Builder.Default private boolean isRead = false;
}
