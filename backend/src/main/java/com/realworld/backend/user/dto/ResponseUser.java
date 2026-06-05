package com.realworld.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUser {
  private ResponseUserDetails user;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ResponseUserDetails {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;
  }
}
