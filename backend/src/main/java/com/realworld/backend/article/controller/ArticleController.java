package com.realworld.backend.article.controller;

import com.realworld.backend.article.dto.RequestArticle;
import com.realworld.backend.article.dto.RequestArticle.createArticleRequest;
import com.realworld.backend.article.dto.RequestArticle.updateArticleRequest;
import com.realworld.backend.article.dto.RequestComment;
import com.realworld.backend.article.dto.ResponseArticle.MultipleArticle;
import com.realworld.backend.article.dto.ResponseArticle.SingleArticle;
import com.realworld.backend.article.dto.ResponseComment.MultipleComment;
import com.realworld.backend.article.dto.ResponseComment.SingleComment;
import com.realworld.backend.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @GetMapping("/")
  public ResponseEntity<MultipleArticle> getListArticle(
      @RequestParam("tag") String tag,
      @RequestParam("author") String author,
      @RequestParam("favorited") String favorited,
      @RequestParam(name = "limit", defaultValue = "1") int limit,
      @RequestParam(name = "offeset", defaultValue = "0") int offset) {

    return new ResponseEntity<>(
        articleService.getListArticles(tag, author, favorited, limit, offset), HttpStatus.OK);
  }

  @GetMapping("/feed")
  public ResponseEntity<MultipleArticle> feedArticles(
      @RequestParam(name = "limit", defaultValue = "0") int limit,
      @RequestParam(name = "offset", defaultValue = "0") int offset) {
    return new ResponseEntity<>(articleService.getFeedArticles(limit, offset), HttpStatus.OK);
  }

  @GetMapping("/{slug}")
  public ResponseEntity<SingleArticle> getSingleArticle(@PathVariable String slug) {
    return new ResponseEntity<>(articleService.getSingleArticle(slug), HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<SingleArticle> postSingleArticle(
      @RequestBody RequestArticle<createArticleRequest> requestArticle) {
    return new ResponseEntity<>(articleService.putSingleArticle(requestArticle.getArticle()),
        HttpStatus.OK);
  }

  @PutMapping("/{slug}")
  public ResponseEntity<SingleArticle> updateArticle(@PathVariable String slug,
      @RequestBody RequestArticle<updateArticleRequest> requestArticle) {
    return new ResponseEntity<>(articleService.updateArticle(slug, requestArticle.getArticle()),
        HttpStatus.OK);
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<?> deleteArticle(@PathVariable String slug) {
    articleService.deleteArticle(slug);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{slug}/comments")
  public ResponseEntity<SingleComment> addCommentsToAnArticle(@PathVariable String slug,
      @RequestBody
      RequestComment requestComment) {
    return new ResponseEntity<>(
        articleService.commentToAnArticle(slug, requestComment.getComment()), HttpStatus.OK);
  }

  @GetMapping("/{slug}/comments")
  public ResponseEntity<MultipleComment> getCommentsFromAnArticle(@PathVariable String slug) {
    return new ResponseEntity<>(articleService.getCommentsFromAnArticle(slug), HttpStatus.OK);
  }

  @DeleteMapping("/{slug}/comments/{id}")
  public ResponseEntity<?> deleteComment(@PathVariable String slug, @PathVariable long id) {
    articleService.deleteComment(slug, id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{slug}/favorite")
  public ResponseEntity<SingleArticle> favoriteArticle(@PathVariable String slug) {
    return new ResponseEntity<>(articleService.favoriteArticle(slug), HttpStatus.OK);
  }

  @DeleteMapping("/{slug}/favorite")
  public ResponseEntity<SingleArticle> unfavoriteArticle(@PathVariable String slug) {
    return new ResponseEntity<>(articleService.unfavoriteArticle(slug), HttpStatus.OK);
  }

}
