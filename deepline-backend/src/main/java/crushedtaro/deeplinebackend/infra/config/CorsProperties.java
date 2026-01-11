package crushedtaro.deeplinebackend.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
  private String[] allowedOrigins;
  private String[] allowedMethods;
  private String[] allowedHeaders;
  private String[] exposedHeaders;
}
