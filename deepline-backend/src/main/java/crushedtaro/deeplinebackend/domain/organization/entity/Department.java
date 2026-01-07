package crushedtaro.deeplinebackend.domain.organization.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import crushedtaro.deeplinebackend.domain.common.BaseEntity;

@Entity
@Table(name = "tbl_department")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {

  @Id
  @Column(name = "dept_code")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int deptCode;

  @Column(name = "dept_name", nullable = false)
  private String deptName;

  @Column(name = "dept_desc")
  private String deptDesc;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private String isDeleted = "N";

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void withdraw() {
    this.isDeleted = "Y";
    this.deletedAt = LocalDateTime.now();
  }
}
