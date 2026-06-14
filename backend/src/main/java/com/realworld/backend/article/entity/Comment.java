package com.realworld.backend.article.entity;

import com.realworld.backend.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long genId;
  @ManyToOne
  @JoinColumn(name = "article_id")
  private Article article;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private String body;
  @ManyToOne
  @JoinColumn(name = "user_username")
  private User author;

  @Builder
  public Comment(Article article, String body, User author) {
    LocalDateTime now = LocalDateTime.now();
    this.article = article;
    createAt = now;
    updateAt = now;
    this.body = body;
    this.author = author;
  }
}
