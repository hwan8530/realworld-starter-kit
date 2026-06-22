package com.realworld.backend.article.repository;

import com.realworld.backend.article.entity.Article;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
  public Optional<Article> findBySlug(String slug);

}
