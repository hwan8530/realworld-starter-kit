package com.realworld.backend.common.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = extractToken(request);
    if (token != null) {
      Authentication authentication = jwtUtil.getAuthentication(token);
      if (authentication != null) {
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      else {
        // token이 있지만 JwtUtil에서 throw된 예외는 AuthenticationEntryPoint에서 처리하도록 request에 예외 정보를 저장
        request.setAttribute("jwtException", new JwtException("유효하지 않은 JWT 토큰"));
      }
    }
    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    String bearerHeader = "Bearer ";
    if (!bearerToken.startsWith(bearerHeader))
      return null;
    return bearerToken;
  }
}
