package crushedtaro.deeplinebackend.domain.approval.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import crushedtaro.deeplinebackend.domain.approval.entity.Approval;
import crushedtaro.deeplinebackend.domain.approval.entity.ApprovalLine;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalStatus;
import crushedtaro.deeplinebackend.domain.approval.enums.ApprovalType;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.repository.MemberRepository;
import crushedtaro.deeplinebackend.domain.organization.entity.Department;
import crushedtaro.deeplinebackend.domain.organization.entity.Position;
import crushedtaro.deeplinebackend.domain.organization.repository.DepartmentRepository;
import crushedtaro.deeplinebackend.domain.organization.repository.PositionRepository;

@DataJpaTest
class ApprovalRepositoryTest {

  @Autowired private ApprovalRepository approvalRepository;
  @Autowired private MemberRepository memberRepository;
  @Autowired private DepartmentRepository departmentRepository;
  @Autowired private PositionRepository positionRepository;

  @Autowired private EntityManager entityManager;

  private Member drafter;
  private Member approver1;
  private Member approver2;
  private Approval savedApproval;

  @BeforeEach
  void setUp() {
    Department department = departmentRepository.save(Department.builder().deptName("개발팀").build());
    Position posDrafter = positionRepository.save(Position.builder().positionName("사원").build());
    Position posApprover = positionRepository.save(Position.builder().positionName("과장").build());

    drafter =
        memberRepository.save(
            Member.builder()
                .memberId("drafter")
                .memberName("김기안")
                .department(department)
                .position(posDrafter)
                .build());
    approver1 =
        memberRepository.save(
            Member.builder()
                .memberId("app1")
                .memberName("박결재")
                .department(department)
                .position(posApprover)
                .build());
    approver2 =
        memberRepository.save(
            Member.builder()
                .memberId("app2")
                .memberName("신최종")
                .department(department)
                .position(posApprover)
                .build());

    Approval approval =
        Approval.builder()
            .title("휴가 신청서")
            .content("테스트 휴가입니다.")
            .member(drafter)
            .approvalType(ApprovalType.VACATION)
            .build();

    approval
        .getApprovalLines()
        .add(
            ApprovalLine.builder()
                .approval(approval)
                .approver(approver1)
                .lineOrder(1)
                .status(ApprovalStatus.PENDING)
                .build());
    approval
        .getApprovalLines()
        .add(
            ApprovalLine.builder()
                .approval(approval)
                .approver(approver2)
                .lineOrder(2)
                .status(ApprovalStatus.WAITING)
                .build());

    savedApproval = approvalRepository.save(approval);
  }

  @Test
  @DisplayName("findDetailByApprovalCode - @EntityGraph 적용 시 N+1 문제 없이 단일 쿼리로 모두 조회되는지 확인")
  void testFindDetailByApprovalCodeWithEntityGraph() {
    entityManager.flush();
    entityManager.clear();

    System.out.println("==[쿼리 실행 시작]==");

    Optional<Approval> result =
        approvalRepository.findDetailByApprovalCode(savedApproval.getApprovalCode());
    //        Optional<Approval> result =
    // approvalRepository.findById(savedApproval.getApprovalCode()); N+1 문제 발생

    System.out.println("==[쿼리 실행 종료]==");

    assertThat(result).isPresent();
    Approval approval = result.get();

    System.out.println("기안자 이름 : " + approval.getMember().getMemberName());
    System.out.println("기안자 부서 : " + approval.getMember().getDepartment().getDeptName());

    List<ApprovalLine> lines = approval.getApprovalLines();

    for (ApprovalLine line : lines) {
      System.out.println("결재자 이름: " + line.getApprover().getMemberName());
      System.out.println("결재자 직급: " + line.getApprover().getPosition().getPositionName());
    }

    assertThat(approval.getApprovalLines()).hasSize(2);
  }
}
