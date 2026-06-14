package com.realworld.backend.common.security.jwt;

import org.springframework.http.HttpHeaders;

public class JwtHeader {

  public static HttpHeaders createJwtHeader(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    return headers;
  }
}
