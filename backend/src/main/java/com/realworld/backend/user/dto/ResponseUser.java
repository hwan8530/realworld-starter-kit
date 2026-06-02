package com.realworld.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUser {
  private String email;
  private String token;
  private String username;
  private String bio;
  private String image;

}
