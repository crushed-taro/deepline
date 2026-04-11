package crushedtaro.deeplinebackend.domain.audit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.audit.annotation.AuditLog;
import crushedtaro.deeplinebackend.domain.audit.entity.Audit;
import crushedtaro.deeplinebackend.domain.audit.repository.AuditLogRepository;
import crushedtaro.deeplinebackend.global.util.SecurityUtil;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

  private final SecurityUtil securityUtil;
  private final AuditLogRepository auditLogRepository;

  @Around("@annotation(auditAnnotation)")
  public Object logAuditActivity(ProceedingJoinPoint pjp, AuditLog auditAnnotation)
      throws Throwable {

    Object result = pjp.proceed();

    try {
      String memberId = securityUtil.getCurrentMemberId();
      String targetId = "UNKNOWN";

      for (Object arg : pjp.getArgs()) {
        if (arg instanceof Long) {
          targetId = String.valueOf(arg);
          break;
        }
      }

      Audit audit =
          Audit.builder()
              .actionUser(memberId)
              .actionType(auditAnnotation.actionType())
              .targetName(auditAnnotation.targetName())
              .targetId(targetId)
              .description("AOP 기록 : " + pjp.getSignature().getName() + " 메서드 완료")
              .build();

      auditLogRepository.save(audit);
      log.debug("[AuditAspect] 감사 로그 저장 완료: {}", auditAnnotation.actionType());
    } catch (Exception e) {
      log.error("[AuditAspect] 감사 로그 저장 중 오류 발생", e);
    }

    return result;
  }
}
