package crushedtaro.deeplinebackend.domain.member.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 604800)
public class RefreshToken {

  @Id private String key;

  private String value;

  public RefreshToken updateValue(String token) {
    this.value = token;
    return this;
  }
}
