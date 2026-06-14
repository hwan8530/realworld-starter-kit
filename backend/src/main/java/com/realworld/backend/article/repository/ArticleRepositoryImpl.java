package com.realworld.backend.article.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.realworld.backend.article.entity.Article;
import static com.realworld.backend.article.entity.QArticle.article;
import static com.realworld.backend.article.entity.QArticleTag.articleTag;
import static com.realworld.backend.article.entity.QFavoriteInfo.favoriteInfo;
import static com.realworld.backend.user.entity.QUser.user;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Article> getArticles(String tag, String author, String favorited, int limit,
      int offset) {
    JPAQuery<Article> query = queryFactory.selectFrom(article);
    joinArticleTagsIfPresent(query, tag);
    joinFavoriteInfoIfPresent(query, favorited);

    return query.fetchJoin().where(eqAuthor(author)).orderBy(article.createdAt.desc()).offset(offset).limit(limit).fetch();
  }

  @Override
  public List<Article> getFeeds(int limit, int offset, List<String> following) {
    return queryFactory.selectFrom(article).join(user).fetchJoin().where(article.author.username.in(following)).orderBy(article.createdAt.desc()).offset(offset).limit(limit).fetch();
  }

  private BooleanExpression eqAuthor(String author) {
    return author != null ? article.author.username.eq(author) : null;
  }

  private void joinArticleTagsIfPresent(JPAQuery<Article> query, String tag) {
    if (tag != null)
    {
      query.join(articleTag, articleTag);
    }
  }

  private void joinFavoriteInfoIfPresent(JPAQuery<Article> query, String favorited) {
    if (favorited != null)
    {
      query.join(favoriteInfo, favoriteInfo);
    }
  }

}
