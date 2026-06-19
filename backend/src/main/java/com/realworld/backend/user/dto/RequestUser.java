package com.realworld.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RequestUser<T> {

  @Getter
  @Setter
  private T user;

  public RequestUser(T user) {
    this.user = user;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Login {

    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String password;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Register {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String password;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateRequest {

    @NotNull
    @NotBlank
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;
  }

}
