package com.realworld.backend.article.dto;

import lombok.Getter;

@Getter
public class RequestComment {

  RequestDetail comment;

  @Getter
  public static class RequestDetail {

    String body;
  }

}
