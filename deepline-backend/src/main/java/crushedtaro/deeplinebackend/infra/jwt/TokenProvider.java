package crushedtaro.deeplinebackend.infra.jwt;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import crushedtaro.deeplinebackend.domain.member.dto.TokenDTO;
import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.member.entity.MemberRole;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

  private static final String AUTHORITIES_KET = "auth";
  private static final String BEARER_TYPE = "bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

  private final UserDetailsService userDetailsService;

  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;

  @PostConstruct
  void init() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    log.info("[TokenProvider] key initialized");
  }

  public TokenDTO generateTokenDTO(Member member) {
    log.info("[TokenProvider] generateTokenDTO() Start");

    List<String> roles = new ArrayList<>();

    for (MemberRole memberRole : member.getMemberRole()) {
      roles.add(memberRole.getAuthority().getAuthorityName());
    }

    log.info("[TokenProvider] authorized authorities {}", roles);

    long now = System.currentTimeMillis();

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

    String accessToken =
        Jwts.builder()
            .setSubject(member.getMemberId())
            .claim(AUTHORITIES_KET, roles)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

    log.info("[TokenProvider] generateTokenDTO() End");

    return new TokenDTO(
        BEARER_TYPE, member.getMemberName(), accessToken, accessTokenExpiresIn.getTime(), null);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.error("[TokenProvider] validateToken() - Invalid JWT Token", e);
      return false;
    }
  }

  public Authentication getAuthentication(String token) {

    log.info("[TokenProvider] getAuthentication() Start");

    Claims claims = parseClaims(token);

    if (claims.get(AUTHORITIES_KET) == null) {
      throw new RuntimeException("Token has no authorities");
    }

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KET).toString().split(","))
            .map(role -> new SimpleGrantedAuthority(role))
            .collect(Collectors.toList());

    log.info("[TokenProvider] authorities extracted: {}", authorities);

    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));

    log.info("[TokenProvider] UserDetails loaded: {}", userDetails);

    log.info("[TokenProvider] getAuthentication() End");

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserId(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
