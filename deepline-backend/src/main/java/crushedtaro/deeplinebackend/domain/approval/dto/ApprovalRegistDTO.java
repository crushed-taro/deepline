package crushedtaro.deeplinebackend.domain.approval.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ApprovalRegistDTO(
    @Schema(description = "결재 문서 제목", example = "2024년 하계 휴가 신청") String title,
    @Schema(description = "결재 문서 내용", example = "개인 사정으로 인한 연차 휴가를 신청합니다.") String content,
    @Schema(description = "결재선 지정 (결재자 회원 코드 목록)", example = "[2, 3]") List<Integer> approverCodes,
    @Schema(description = "결재 유형 (GENERAL, VACATION)", example = "VACATION") String type,
    @Schema(description = "시작일 (휴가인 경우 필수)", example = "2024-07-20") LocalDate startDate,
    @Schema(description = "종료일 (휴가인 경우 필수)", example = "2024-07-25") LocalDate endDate) {}
