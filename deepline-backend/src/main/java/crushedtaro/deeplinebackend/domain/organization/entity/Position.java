package crushedtaro.deeplinebackend.domain.organization.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_position")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {

  @Id
  @Column(name = "position_code")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int positionCode;

  @Column(name = "position_name", nullable = false)
  private String positionName;

  @Column(name = "position_order")
  private String positionOrder;

  @Column(name = "position_desc")
  private String positionDesc;
}
