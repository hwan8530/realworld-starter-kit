package com.realworld.backend.common.security.jwt;

import java.util.Collection;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private final Object principal; // 사용자 정보 (예: username, email 등)
  private Object credentials; // JWT 토큰 자체 (필요시)


  // 인증되지 않은 상태로 토큰 생성 (예: 로그인 시도)
  public JwtAuthenticationToken (Object credentials) {
    super((Collection<? extends GrantedAuthority>) null);
    this.principal = null;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  // 인증된 상태로 토큰 생성 (JWT 토큰 검증 완료)
  public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
  {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(true);
  }

  @Override
  public @Nullable Object getCredentials() {
    return this.credentials;
  }

  @Override
  public @Nullable Object getPrincipal() {
    return this.principal;
  }
}
