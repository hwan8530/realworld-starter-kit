package com.realworld.backend.article.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponseTag {

  List<String> tags;

  public ResponseTag(List<String> tags) {
    this.tags = tags;
  }
}
