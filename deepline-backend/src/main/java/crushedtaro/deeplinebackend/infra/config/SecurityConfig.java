package crushedtaro.deeplinebackend.infra.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

import crushedtaro.deeplinebackend.infra.jwt.JwtAccessDeniedHandler;
import crushedtaro.deeplinebackend.infra.jwt.JwtAuthenticationEntryPoint;
import crushedtaro.deeplinebackend.infra.jwt.JwtFilter;
import crushedtaro.deeplinebackend.infra.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final TokenProvider tokenProvider;

  private final CorsProperties corsProperties;

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        """
      ROLE_ADMIN > ROLE_HR
      ROLE_HR > ROLE_USER
      """);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    String[] allowedOrigins = corsProperties.getAllowedOrigins();
    String[] allowedMethods = corsProperties.getAllowedMethods();
    String[] allowedHeaders = corsProperties.getAllowedHeaders();
    String[] exposedHeaders = corsProperties.getExposedHeaders();

    config.setAllowedOrigins(List.of(allowedOrigins));

    config.setAllowedMethods(List.of(allowedMethods));

    config.setAllowedHeaders(List.of(allowedHeaders));

    config.setExposedHeaders(List.of(exposedHeaders));

    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(
            exception -> {
              exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
              exception.accessDeniedHandler(jwtAccessDeniedHandler);
            })
        .authorizeHttpRequests(
            auth -> {
              auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
              auth.requestMatchers("/api/v1/auth/**").permitAll();

              auth.requestMatchers(HttpMethod.PUT, "/api/v1/members/*/assign").hasRole("HR");

              auth.requestMatchers(HttpMethod.GET, "/api/v1/members").hasRole("USER");

              auth.requestMatchers("/api/v1/members/**").hasRole("USER");

              auth.requestMatchers("/api/v1/organization/**").hasRole("ADMIN");

              auth.requestMatchers("/api/**").hasRole("USER");
              auth.anyRequest().permitAll();
            })
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> {})
        .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
