package com.realworld.backend.article.controller;

import com.realworld.backend.article.dto.ResponseTag;
import com.realworld.backend.article.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  @GetMapping("/api/tags")
  public ResponseEntity<ResponseTag> listOfTags() {
    return new ResponseEntity<>(tagService.listOfTags(), HttpStatus.OK);
  }
}
