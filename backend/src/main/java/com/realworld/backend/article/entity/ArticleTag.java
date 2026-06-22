package com.realworld.backend.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArticleTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id")
  private Article article;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_tagName", referencedColumnName = "tagName")
  private Tag tag;

  @Builder
  public ArticleTag(Article article, Tag tag) {
    this.article = article;
    this.tag = tag;
  }
}
