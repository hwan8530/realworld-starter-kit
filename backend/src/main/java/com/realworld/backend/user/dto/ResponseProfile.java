package com.realworld.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseProfile {
  private ResponseProfileDetails profile;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ResponseProfileDetails {
    private String username;
    private String bio;
    private String image;
    private boolean following;
  }
}
