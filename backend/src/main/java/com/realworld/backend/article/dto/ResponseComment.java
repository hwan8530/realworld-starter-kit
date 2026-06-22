package com.realworld.backend.article.dto;

import com.realworld.backend.user.dto.ResponseProfile;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ResponseComment {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CommentDetails {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private ResponseProfile author;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class SingleComment {

    CommentDetails comment;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class MultipleComment {

    List<CommentDetails> comments;
  }

}
