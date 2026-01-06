package crushedtaro.deeplinebackend.domain.notice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;
import crushedtaro.deeplinebackend.domain.member.entity.Member;

@Entity
@Table(name = "tbl_notice")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notice_code")
  private Long noticeCode;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_code")
  private Member author;

  @Column(name = "view_count")
  @Builder.Default
  private int viewCount = 0;

  @Column(name = "is_pinned")
  @Builder.Default
  private boolean isPinned = false;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private String isDeleted = "N";

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void incrementViewCount() {
    this.viewCount++;
  }

  public void withdraw() {
    this.isDeleted = "Y";
    this.deletedAt = LocalDateTime.now();
  }

  public void updateNotice(String title, String content, boolean isPinned) {
    this.title = title;
    this.content = content;
    this.isPinned = isPinned;
  }
}
