package com.realworld.backend.article.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RequestArticle<T> {
  private T article;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class createArticleRequest {
    String title;
    String description;
    String body;
    List<String> tagList;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class updateArticleRequest {
    String title;
    String description;
    String body;
  }
}
