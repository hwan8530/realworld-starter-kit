package com.realworld.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RequestUser {

  @Getter
  @AllArgsConstructor
  public static class Login {
    private String email;
    private String password;
  }

  @Getter
  @AllArgsConstructor
  static class Registration {
    private String username;
    private String email;
    private String password;
  }

  @Getter
  @AllArgsConstructor
  static class Update {
    private String email;
    private String bio;
    private String image;
  }

}
