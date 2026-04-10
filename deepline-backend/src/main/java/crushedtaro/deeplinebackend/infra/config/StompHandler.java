package crushedtaro.deeplinebackend.infra.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.global.exception.CustomException;
import crushedtaro.deeplinebackend.global.exception.ErrorCode;
import crushedtaro.deeplinebackend.infra.jwt.TokenProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

  private final TokenProvider tokenProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT == accessor.getCommand()) {
      String jwt = accessor.getFirstNativeHeader("Authorization");

      if (jwt != null && jwt.startsWith("Bearer ")) {
        jwt = jwt.substring(7);
      }

      if (!tokenProvider.validateToken(jwt)) {
        log.error("WebSocket 토큰 검증 실패");
        throw new CustomException(ErrorCode.WEBSOCKET_AUTH_ERROR);
      }

      String userId = tokenProvider.getUserId(jwt);
      log.info("WebSocket 연결 성공: 사용자 = {}, 세션 ID = {}", userId, accessor.getSessionId());
    }
    return message;
  }
}
