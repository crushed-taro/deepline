package crushedtaro.deeplinebackend.domain.audit.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;

@Entity
@Table(name = "tbl_audit_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "audit_code")
  private Long auditCode;

  @Column(name = "action_user", nullable = false)
  private String actionUser;

  @Column(name = "action_type", nullable = false)
  private String actionType;

  @Column(name = "target_name", nullable = false)
  private String targetName;

  @Column(name = "target_id")
  private String targetId;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;
}
