package com.realworld.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseProfile {
  private String username;
  private String bio;
  private String image;
  private boolean following;
}
