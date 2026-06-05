package com.realworld.backend.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FavoriteInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_username")
  String username;
  @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "article_title")
  String title;
}
