package crushedtaro.deeplinebackend.domain.chat.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Table(name = "tbl_chat_room")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chatRoomCode;

  @Column(unique = true, nullable = false)
  private String roomId;

  private String roomName;

  @Enumerated(EnumType.STRING)
  private ChatType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member1_code")
  private Member member1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member2_code")
  private Member member2;

  public enum ChatType {
    PERSONAL,
    DEPARTMENT
  }
}
