package com.realworld.backend.mapper;

import com.realworld.backend.article.dto.ResponseArticle.ArticleDetails;
import com.realworld.backend.article.dto.ResponseComment.CommentDetails;
import com.realworld.backend.article.entity.Article;
import com.realworld.backend.article.entity.ArticleTag;
import com.realworld.backend.article.entity.Comment;
import com.realworld.backend.user.dto.ResponseProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

  ArticleMapper articleMapper = Mappers.getMapper(ArticleMapper.class);

  @Mapping(source = "article.articleTags", target = "tagList")
  @Mapping(source = "favorited", target = "favorited")
  public ArticleDetails articleToDetails(Article article, boolean favorited, int favoritesCount,
      ResponseProfile author);

  default String articleTagsToString(ArticleTag articleTag) {
    if (articleTag == null) {
      return null;
    } else {
      return articleTag.getTag().getTagName();
    }
  }

  @Mapping(source = "id", target = "id")
  public CommentDetails commentToDetails(Comment comment, Long id, ResponseProfile author);
}
