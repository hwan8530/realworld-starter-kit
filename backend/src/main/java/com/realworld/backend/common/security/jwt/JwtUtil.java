package com.realworld.backend.common.security.jwt;

import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.service.CustomUserDetails;
import com.realworld.backend.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final long expirationTime;
  private final Key secret;
  private final CustomUserDetailsService userDetailsService;

  public JwtUtil(@Value("${jwt.expiration}") long expirationTime,
      @Value("${jwt.secret}") String secretKey, CustomUserDetailsService userDetailsService) {
    this.expirationTime = expirationTime;
    this.secret = Keys.hmacShaKeyFor(secretKey.getBytes());
    this.userDetailsService = userDetailsService;
  }

  public String createAuthToken(Login login) {
    return generateToken(login);
  }

  /*
   * JWT 토큰 생성
   * - issuer : 토큰 발급자
   * - subject : 토큰 주체
   * - issuedAt : 발급 시간
   * - expiration : 만료 시간
   * - refreshToken 없이 accessToken만 생성 및 검증
   * login 처리를 겸해야하니 email, password를 통해 userDetailService에서 사용자 정보를 조회 후 토큰 생성
   * 토큰 생성시 username 등을 포함해야하고 별도의 메소드에서 userDetails 검증을 진행하면 DB 접근이 2번 발생하므로 아래 함수에서 처리
   */
  private String generateToken(Login login) {
    try {
      UserDetails userDetails = userDetailsService.loadUserByEmail(login.getEmail());
      Date now = new Date();
      return Jwts.builder()
          .issuer("realworld")
          .subject(login.getEmail())
          .issuedAt(now)
          .expiration(new Date(now.getTime() + expirationTime))
          .claim("username", userDetails.getUsername())
          .signWith(secret)
          .compact();
    } catch (UsernameNotFoundException e) {
      throw new RuntimeException("사용자 정보가 올바르지 않습니다.");
    }
  }

  private boolean loginValidate(Login login) {
    try {
      UserDetails userDetails = userDetailsService.loadUserByEmail(login.getEmail());
      return Objects.requireNonNull(userDetails.getPassword()).equals(login.getPassword());
    } catch (UsernameNotFoundException e) {
      throw new RuntimeException("사용자 정보가 올바르지 않습니다.");
    }
  }

  /*
   * JWT 토큰에서 이메일 추출
   * - 토큰 검증 후 payload에서 subject(sub) 클레임을 추출
   */
  public String getEmailFromToken(Claims payload) {
    return payload.get("sub", String.class);
  }

  /*
   * JWT 토큰에서 username 추출
   */
  public String getUsernameFromToken(Claims payload) {
    return payload.get("username", String.class);
  }

  /*
   * JWT 토큰에서 payload 추출
   */
  private Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) secret)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /*
   * JWT 토큰기반 인증 객체 생성
   * - 토큰에서 이메일 추출 후 Authentication 객체 생성
   * - JWT 토큰 검증 후 인증 객체 생성
   */
  public Authentication getAuthentication(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      CustomUserDetails userDetails = userDetailsService.loadUserByEmail(getEmailFromToken(claims));
      return new JwtAuthenticationToken(userDetails.getUsername(), token,
          userDetails.getAuthorities());
    } catch (UsernameNotFoundException e) {
      throw new RuntimeException("계정이 존재하지 않습니다.");
    } catch (SignatureException e) {
      throw new RuntimeException("유효하지 않은 JWT 토큰");
    }
  }
}
