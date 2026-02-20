package crushedtaro.deeplinebackend.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {
  private String host;
  private int port;
  private String username;
  private String password;

  private Stomp stomp;

  @Getter
  @Setter
  public static class Stomp {
    private int port;
  }
}
