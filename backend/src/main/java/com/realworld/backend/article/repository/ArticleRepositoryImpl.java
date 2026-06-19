package com.realworld.backend.article.repository;

import static com.realworld.backend.article.entity.QArticle.article;
import static com.realworld.backend.article.entity.QArticleTag.articleTag;
import static com.realworld.backend.article.entity.QFavoriteInfo.favoriteInfo;
import static com.realworld.backend.user.entity.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.realworld.backend.article.entity.Article;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Article> getArticles(String tag, String author, String favorited, int limit,
      int offset) {

    JPAQuery<Article> query = queryFactory.selectFrom(article);
    joinArticleTagsIfPresent(query, tag);
    joinFavoriteInfoIfPresent(query, favorited);
    return query.where(eqAuthor(author).and(inTagList(tag)).and(inFavoriteInfo(favorited)))
        .orderBy(article.createdAt.desc()).offset(offset)
        .limit(limit).fetch();
  }

  @Override
  public List<Article> getFeeds(int limit, int offset, List<String> following) {
    return queryFactory.selectFrom(article).join(article.author, user)
        .fetchJoin()
        .where(article.author.username.in(following)).orderBy(article.createdAt.desc())
        .offset(offset).limit(limit).fetch();
  }

  private BooleanExpression eqAuthor(String author) {
    return (author != null && !author.isBlank()) ? article.author.username.eq(author) : null;
  }

  private BooleanExpression inTagList(String tag) {
    return (tag != null && !tag.isBlank()) ? article.articleTags.any().tag.tagName.eq(tag) : null;
  }

  private BooleanExpression inFavoriteInfo(String favorited) {
    return (favorited != null && !favorited.isBlank())
        ? article.favoriteInfos.any().user.username.eq(favorited) : null;
  }

  private void joinArticleTagsIfPresent(JPAQuery<Article> query, String tag) {
    if (tag != null && !tag.isBlank()) {
      query.join(article.articleTags, articleTag).fetchJoin();
      query.join(articleTag.tag).fetchJoin();
    }
  }

  private void joinFavoriteInfoIfPresent(JPAQuery<Article> query, String favorited) {
    if (favorited != null && !favorited.isBlank()) {
      query.join(article.favoriteInfos, favoriteInfo).fetchJoin();
    }
  }

}
