package crushedtaro.deeplinebackend.domain.audit.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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

  private final ExpressionParser expressionParser = new SpelExpressionParser();
  private final ParameterNameDiscoverer parameterNameDiscoverer =
      new DefaultParameterNameDiscoverer();

  @Around("@annotation(auditAnnotation)")
  public Object logAuditActivity(ProceedingJoinPoint pjp, AuditLog auditAnnotation)
      throws Throwable {

    Object result = pjp.proceed();

    try {
      String memberId = securityUtil.getCurrentMemberId();
      String targetId = getTargetIdBySpel(pjp, auditAnnotation);

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

  private String getTargetIdBySpel(ProceedingJoinPoint pjp, AuditLog auditLog) {

    String key = auditLog.key();

    if (key.isEmpty()) return "UNKNOWN";

    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    Object[] args = pjp.getArgs();

    String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
    StandardEvaluationContext context = new StandardEvaluationContext();

    if (parameterNames != null) {
      for (int i = 0; i < parameterNames.length; i++) {
        context.setVariable(parameterNames[i], args[i]);
      }
    }

    try {
      Object value = expressionParser.parseExpression(key).getValue(context);
      return value != null ? value.toString() : "UNKNOWN";
    } catch (Exception e) {
      log.warn("[AuditAspect] SpEL 파싱 실패 : {}", key);
      return "UNKNOWN";
    }
  }
}
