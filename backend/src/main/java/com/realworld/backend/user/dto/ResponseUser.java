package com.realworld.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUser {
  private String email;
  private String token;
  private String username;
  private String bio;
  private String image;

}
