package com.realworld.backend.article.service;

import com.realworld.backend.article.dto.RequestArticle.createArticleRequest;
import com.realworld.backend.article.dto.RequestArticle.updateArticleRequest;
import com.realworld.backend.article.dto.RequestComment.RequestDetail;
import com.realworld.backend.article.dto.ResponseArticle.ArticleDetails;
import com.realworld.backend.article.dto.ResponseArticle.MultipleArticle;
import com.realworld.backend.article.dto.ResponseArticle.SingleArticle;
import com.realworld.backend.article.dto.ResponseComment.CommentDetails;
import com.realworld.backend.article.dto.ResponseComment.MultipleComment;
import com.realworld.backend.article.dto.ResponseComment.SingleComment;
import com.realworld.backend.article.entity.Article;
import com.realworld.backend.article.entity.Comment;
import com.realworld.backend.article.entity.FavoriteInfo;
import com.realworld.backend.article.entity.Tag;
import com.realworld.backend.article.repository.ArticleRepository;
import com.realworld.backend.article.repository.CommentRepository;
import com.realworld.backend.article.repository.FavoriteInfoRepository;
import com.realworld.backend.article.repository.TagRepository;
import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.mapper.ArticleMapper;
import com.realworld.backend.mapper.UserMapper;
import com.realworld.backend.user.dto.ResponseProfile;
import com.realworld.backend.user.entity.Follow;
import com.realworld.backend.user.entity.User;
import com.realworld.backend.user.repository.FollowRepository;
import com.realworld.backend.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final FollowRepository followRepository;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;
  private final UserMapper userMapper;
  private final ArticleMapper articleMapper;
  private final CommentRepository commentRepository;
  private final FavoriteInfoRepository favoriteInfoRepository;

  public MultipleArticle getListArticles(String tag, String author, String favorited, Integer limit,
      int offset) {
    // 인증은 옵션
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUser = Objects.requireNonNull(
        Objects.requireNonNull(authentication).getPrincipal()).toString();
    log.debug("tag : {}", tag);
    log.debug("author : {}", author);
    log.debug("favorited : {}", favorited);
    log.debug("limit : {}", limit);
    log.debug("offset : {}", offset);
    List<Article> articleList = articleRepository.getArticles(tag, author, favorited, limit,
        offset);
    log.debug("artricleList : {}", articleList.size());
    MultipleArticle multipleArticle = new MultipleArticle(new ArrayList<ArticleDetails>(),
        articleList.size());
    for (Article article : articleList) {
      multipleArticle.getArticles()
          .add(articleToDetails(article, authentication.isAuthenticated(), currentUser));
    }
    return multipleArticle;
  }

  public MultipleArticle getFeedArticles(Integer limit, int offset) {
    Authentication authentication = requireAuthentication();
    String currentUser = Objects.requireNonNull(authentication.getPrincipal()).toString();
    List<String> followNames = followRepository.findByFromUserNameInString(currentUser);
    List<Article> articleList = articleRepository.getFeeds(limit, offset, followNames);

    MultipleArticle multipleArticle = new MultipleArticle(new ArrayList<>(), articleList.size());
    for (Article article : articleList) {
      multipleArticle.getArticles()
          .add(articleToDetails(article, authentication.isAuthenticated(), currentUser));
    }
    return multipleArticle;
  }

  public SingleArticle getSingleArticle(String slug) {
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));
    return new SingleArticle(articleToDetails(article, false, ""));
  }

  @Transactional
  public SingleArticle putSingleArticle(createArticleRequest request) {
    Authentication authentication = requireAuthentication();
    User currentUser = userRepository.findByUsername(
            Objects.requireNonNull(authentication.getPrincipal()).toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    LocalDateTime now = LocalDateTime.now();

    Article article = Article.builder()
        .slug(titleToSlug(request.getTitle()))
        .title(request.getTitle())
        .description(request.getDescription())
        .body(request.getBody())
        .createdAt(now)
        .updatedAt(now)
        .author(currentUser)
        .build();
    articleRepository.save(article);

    // tagList를 순회하면서 DB에 없으면 Tag 부터 추가
    for (String tagName : request.getTagList()) {
      tagName = tagName.toLowerCase();
      Optional<Tag> optionalTag = tagRepository.findByTagName(tagName);
      if (optionalTag.isPresent()) {
        article.addTag(optionalTag.get());
      } else {
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);
        article.addTag(tag);
      }
    }
    return new SingleArticle(
        articleToDetails(article, authentication.isAuthenticated(), currentUser.getUsername()));

  }

  @Transactional
  public SingleArticle updateArticle(String slug, updateArticleRequest request) {
    Authentication authentication = requireAuthentication();
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));
    if (request.getTitle() != null) {
      article.setTitle(request.getTitle());
      article.setSlug(titleToSlug(request.getTitle()));
    }
    if (request.getDescription() != null) {
      article.setDescription(request.getDescription());
    }

    if (request.getBody() != null) {
      article.setBody(request.getBody());
    }

    return new SingleArticle(
        articleToDetails(article, authentication.isAuthenticated(), Objects.requireNonNull(
            authentication.getPrincipal()).toString()));
  }

  @Transactional
  public void deleteArticle(String slug) {
    Authentication authentication = requireAuthentication();
    String currentUser = Objects.requireNonNull(authentication.getPrincipal()).toString();
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));

    if (article.getAuthor().getUsername().equals(currentUser)) {
      articleRepository.delete(article);
    }
  }

  @Transactional
  public SingleComment commentToAnArticle(String slug, RequestDetail request) {
    Authentication authentication = requireAuthentication();
    User curretUser = userRepository.findByUsername(
            Objects.requireNonNull(authentication.getPrincipal()).toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));
    Comment comment = article.addComment(request.getBody(), curretUser);
    ResponseProfile profile = new ResponseProfile(userMapper.userToProfile(curretUser, false));
    return new SingleComment(
        articleMapper.commentToDetails(comment, (long) article.getComments().size(), profile));
  }

  public MultipleComment getCommentsFromAnArticle(String slug) {
    // authentication optional
    Optional<User> optionalUser = userRepository.findByUsername(Objects.requireNonNull(
        SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));

    List<CommentDetails> comments = new ArrayList<>();
    int i = 0;
    boolean follow = false;
    log.debug("size of comments : {}", article.getComments().size());
    for (Comment comment : article.getComments()) {
      if (optionalUser.isPresent()) {
        Optional<Follow> optionalFollow = followRepository.findByFromUserNameAndToUserName(
            optionalUser.get().getUsername(), comment.getAuthor().getUsername());
        if (optionalFollow.isPresent()) {
          follow = true;
        }
      } else {
        follow = false;
      }
      comments.add(articleMapper.commentToDetails(comment, (long) i + 1,
          new ResponseProfile(userMapper.userToProfile(comment.getAuthor(), follow))));
      i++;
    }
    return new MultipleComment(comments);
  }

  @Transactional
  public void deleteComment(String slug, long id) {
    Authentication authentication = requireAuthentication();
    String currentUser = Objects.requireNonNull(authentication.getPrincipal()).toString();
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));

    Comment comment = article.getComments().get((int) id - 1);
    if (!comment.getAuthor().getUsername().equals(currentUser)) {
      throw new CustomException(CustomExceptionList.UNAUTHORIZED_REQUEST);
    }
    article.getComments().remove(comment);
    commentRepository.delete(comment);
  }

  @Transactional
  public SingleArticle favoriteArticle(String slug) {
    Authentication authentication = requireAuthentication();
    User currentUser = userRepository.findByUsername(
            Objects.requireNonNull(authentication.getPrincipal()).toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));
    article.addFavorite(currentUser);
    return new SingleArticle(articleToDetails(article, authentication.isAuthenticated(),
        currentUser.getUsername()));
  }

  @Transactional
  public SingleArticle unfavoriteArticle(String slug) {
    Authentication authentication = requireAuthentication();
    String currentUser = Objects.requireNonNull(authentication.getPrincipal()).toString();
    Optional<FavoriteInfo> favoriteInfo = favoriteInfoRepository.findByUserNameAndArticleSlug(
        currentUser,
        slug);
    if (favoriteInfo.isPresent()) {
      favoriteInfoRepository.delete(favoriteInfo.get());
      favoriteInfoRepository.flush();
    } else {
      throw new CustomException(CustomExceptionList.NOT_FOUND_REQUEST);
    }

    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(() -> new CustomException(CustomExceptionList.ARTICLE_NOT_FOUND));
    return new SingleArticle(
        articleToDetails(article, authentication.isAuthenticated(), currentUser));

  }

  public ArticleDetails articleToDetails(Article article, boolean authenticated,
      String currentUser) {
    boolean following = false;
    boolean isMyFavorite = false;
    if (authenticated && currentUser != null) {
      following = followRepository.findByFromUserNameAndToUserName(currentUser,
          article.getAuthor().getUsername()).isPresent();
      isMyFavorite = article.getFavoriteUsers().contains(currentUser);
    }
    log.debug("articletoDetails() getAuthor:{}", article.getAuthor().getUsername());
    return articleMapper.articleToDetails(article, isMyFavorite, article.getFavoriteInfos().size(),
        userMapper.userToProfile(article.getAuthor(), following));
  }

  public String titleToSlug(String title) {
    String slug = title.toLowerCase().replace(" ", "-");
    if (articleRepository.findBySlug(slug).isPresent()) {
      // slug 중복으로 중복되지 않도록 만들어야 함
      // localdatetime을 붙이면 가장 안겹칠거 같긴한데 너무 식별이 어렵지 않나
      return slug.concat(LocalDateTime.now().toString());
    }
    return slug;
  }

  public Authentication requireAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.UNAUTHORIZED_REQUEST);
    }
    return authentication;
  }
}
