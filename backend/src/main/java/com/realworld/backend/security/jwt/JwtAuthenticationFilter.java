package com.realworld.backend.security.jwt;

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


  //TODO : Body에서 Login 정보 추출해서 토큰 생성시에 넣어주는 로직 필요
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = extractToken(request);
    if (token != null && jwtUtil.validateOnlyToken(token)) {
      Authentication authentication = jwtUtil.getAuthentication(token, null);
      if (authentication != null) {
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
      }
    } else {
      request.setAttribute("jwtException", new JwtException("유효하지 않은 JWT 토큰"));
      SecurityContextHolder.clearContext();
      filterChain.doFilter(request, response);
    }
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    String bearerHeader = "Bearer ";
    if (!bearerToken.startsWith(bearerHeader))
      return null;
    return bearerToken;
  }
}
