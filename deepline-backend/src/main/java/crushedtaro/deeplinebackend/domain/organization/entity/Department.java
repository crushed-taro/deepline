package crushedtaro.deeplinebackend.domain.organization.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_department")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

  @Id
  @Column(name = "dept_code")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int deptCode;

  @Column(name = "dept_name", nullable = false)
  private String deptName;

  @Column(name = "dept_desc")
  private String deptDesc;
}
