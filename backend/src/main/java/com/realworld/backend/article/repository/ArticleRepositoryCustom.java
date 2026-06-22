package com.realworld.backend.article.repository;

import com.realworld.backend.article.entity.Article;
import java.util.List;

public interface ArticleRepositoryCustom {

  List<Article> getArticles(String tag, String author, String favorited, int limit, int offset);

  List<Article> getFeeds(int limit, int offset, List<String> following);
}
