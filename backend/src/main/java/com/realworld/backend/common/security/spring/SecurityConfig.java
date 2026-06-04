package com.realworld.backend.common.security.spring;

import com.realworld.backend.common.security.jwt.JwtAuthenticationEntryPoint;
import com.realworld.backend.common.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // 1. CORS 기본 설정, CSRF 비활성화
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowCredentials(true);
          config.setAllowedOrigins(List.of("*"));
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
          config.setAllowedHeaders(List.of("*"));
          return config;
        }))
        .csrf(AbstractHttpConfigurer::disable)

        // 2. 경로별 권한 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users", "/api/users/login", "/api/tags").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/articles/*", "/api/articles/**/comments").permitAll()
            .requestMatchers("/api/user", "/api/profiles/**", "/api/articles/**").authenticated())

        // 3. 예외 처리 등록 (AuthenticationEntryPoint)
        .exceptionHandling(exhandle -> exhandle.authenticationEntryPoint(jwtAuthenticationEntryPoint))

        // 4. JWT 필터
        .addFilterBefore(jwtAuthenticationFilter, jwtAuthenticationFilter.getClass());

    return http.build();
  }

}
