package crushedtaro.deeplinebackend.domain.notification.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_notification")
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_code")
  private Long notificationCode;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private String url;

  @Column(name = "is_read")
  @Builder.Default
  private boolean isRead = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_code")
  private Member receiver;

  public void read() {
    this.isRead = true;
  }
}
