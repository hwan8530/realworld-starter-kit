package com.realworld.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RequestUser<T> {
  @Getter
  @Setter
  private T user;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Login {
    private String email;
    private String password;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Register {
    private String username;
    private String email;
    private String password;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Update {
    private String email;
    private String bio;
    private String image;
  }

}
