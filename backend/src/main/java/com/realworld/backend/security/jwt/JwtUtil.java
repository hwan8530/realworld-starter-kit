package com.realworld.backend.security.jwt;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final long expirationTime;
  private final Key secret;
  private final CustomUserDetailsService userDetailsService;
  public JwtUtil(@Value("${jwt.expiration_time}") long expirationTime, @Value("${jwt.secret}") String secretKey, CustomUserDetailsService userDetailsService) {
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
  */
  private String generateToken(Login login) {
    if (!loginValidate(login))
      return null;

    Date now = new Date();
      return Jwts.builder()
          .issuer("realworld")
          .subject(login.getEmail())
          .issuedAt(now)
          .expiration(new Date(now.getTime() + expirationTime))
          .signWith(secret)
          .compact();
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
  public String getEmailFromToken(String token){
    return getClaimsFromToken(token)
        .get("sub", String.class);
  }

  public String getUsernameFromToken(String token){
    return getClaimsFromToken(token)
        .get("username", String.class);
  }

  private Claims getClaimsFromToken(String token){
    return Jwts.parser()
        .verifyWith((SecretKey) secret)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /*
  * JWT 토큰 검증
  * - 토큰 자체만 검증
  * - 토큰이 유효한지, 서명이 올바른지, 만료되지 않았는지 확인
  * - parseSignedClaims 메서드에서 Exception이 발생하면 유효하지 않은 토큰으로 간주
  */
  public boolean validateOnlyToken(String token) {
    try {
      Jwts.parser()
          .verifyWith((SecretKey) secret)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (SignatureException e) {
      return false;
    }
  }

  /*
  * JWT 토큰 검증 및 사용자 정보 확인
  * token의 유효성 검증 + 토큰에서 추출한 정보와 body로 받은 인증 정보가 일치하는지 확인
  */
  public boolean validateToken(String token, Login login) {
    String email = getEmailFromToken(token);
    return validateOnlyToken(token) && email.equals(login.getEmail()) && loginValidate(new Login(email, login.getPassword()));
  }

  /*
  * JWT 토큰기반 인증 객체 생성
  * - 토큰에서 이메일 추출 후 Authentication 객체 생성
  * - JWT 토큰 검증 후 인증 객체 생성
  */
  public Authentication getAuthentication(String token, Login login) {
    try {
      UserDetails userDetails = userDetailsService.loadUserByEmail(login.getEmail());
      // 다른 로직에 앞서 login 정보와 db에서 조회한 사용자 정보가 일치하는지 확인
      if (!Objects.requireNonNull(userDetails.getPassword()).equals(login.getPassword()))
        return null;

      // token 이 null이면 최초 로그인 시도 -> 토큰 생성 후 인증 객체 반환
      if (token == null) {
        return new JwtAuthenticationToken(userDetails.getUsername(), generateToken(login), userDetails.getAuthorities());
      }
      // token이 있다면 검증 후 인증 객체 반환
      String email = getEmailFromToken(token);
      if (!email.equals(login.getEmail()))
        return null;

      return new JwtAuthenticationToken(userDetails.getUsername(), token, userDetails.getAuthorities());

    } catch (UsernameNotFoundException e) {
      throw new RuntimeException("사용자 정보가 올바르지 않습니다.");
    }

  }
}
