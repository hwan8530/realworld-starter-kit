package com.realworld.backend.article.dto;

import com.realworld.backend.user.dto.ResponseProfile;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ResponseArticle {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SingleArticle {
    ArticleDetails article;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MultipleArticle {
    List<ArticleDetails> articles;
    int articlesCount;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ArticleDetails {
    String slug;
    String title;
    String description;
    String body;
    List<String> tagList;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
    boolean favorited;
    int favoritesCount;
    ResponseProfile author;
  }

}
