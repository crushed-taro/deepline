package crushedtaro.deeplinebackend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
              auth.requestMatchers("/").authenticated();
              auth.requestMatchers("/api/v1/auth/**").permitAll();

              auth.requestMatchers(HttpMethod.PUT, "/api/v1/members/*/assign").hasRole("HR");

              auth.requestMatchers(HttpMethod.GET, "/api/v1/members").hasAnyRole("HR", "ADMIN");

              auth.requestMatchers("/api/v1/members/**").permitAll();
              auth.requestMatchers("/api/**").hasAnyRole("USER", "ADMIN", "HR");
              auth.anyRequest().permitAll();
            })
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> {})
        .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
