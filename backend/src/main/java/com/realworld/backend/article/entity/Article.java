package com.realworld.backend.article.entity;

import com.realworld.backend.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String slug;
  private String title;
  private String description;
  private String body;
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ArticleTag> articleTags;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FavoriteInfo> favoriteInfos;
  @ManyToOne
  @JoinColumn(name = "user_username")
  private User author;
  @OneToMany(mappedBy = "genId", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createAt ASC")
  private List<Comment> comments;

  @Builder
  public Article(String slug, String title, String description, String body,
      LocalDateTime createdAt, LocalDateTime updatedAt, User author) {
    this.slug = slug;
    this.title = title;
    this.description = description;
    this.body = body;
    this.articleTags = new ArrayList<>();
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.favoriteInfos = new ArrayList<>();
    this.author = author;
    this.comments = new ArrayList<>();
  }

  public List<String> getTagList() {
    return articleTags.stream().map(articleTag -> articleTag.getTag().getTagName()).collect(
        Collectors.toList());
  }

  public List<String> getFavoriteUsers() {
    return favoriteInfos.stream().map(favoriteInfo -> favoriteInfo.getUser().getUsername())
        .collect(Collectors.toList());
  }

  // articleTags 삽입 메소드
  public void addTag(Tag tag) {
    ArticleTag articleTag = ArticleTag.builder().article(this).tag(tag).build();
    this.articleTags.add(articleTag);
  }

  public void addFavorite(User user) {
    FavoriteInfo favoriteInfo = FavoriteInfo.builder().article(this).user(user).build();
    this.favoriteInfos.add(favoriteInfo);
  }

  // Comments 삽입 메소드
  public Comment addComment(String body, User author) {
    Comment comment = Comment.builder().article(this).body(body).author(author).build();
    this.comments.add(comment);
    return comment;
  }
}

