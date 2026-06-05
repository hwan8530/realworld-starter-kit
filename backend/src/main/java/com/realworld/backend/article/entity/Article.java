package com.realworld.backend.article.entity;

import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Article {
  String slug;
  String title;
  String description;
  String body;
  Date createdAt;
  Date updatedAt;
  String authorName;
}
