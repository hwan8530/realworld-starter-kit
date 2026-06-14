package com.realworld.backend.article.repository;

import com.realworld.backend.article.entity.FavoriteInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteInfoRepository extends JpaRepository<FavoriteInfo, Long> {

  @Query("select f from FavoriteInfo f join fetch f.user join fetch f.article where f.user.username = :username and f.article.slug = :slug")
  public Optional<FavoriteInfo> findByUserNameAndArticleSlug(
      @Param("username") String username,
      @Param("slug") String slug);
}
