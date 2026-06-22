package com.realworld.backend.common.errorhandling.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionList {
  REQUEST_FAILED(422, "can't be empty"),
  UNAUTHORIZED_REQUEST(401, "Unauthorized request"),
  FORBIDDEN_REQUEST(403, "Forbidden request"),
  NOT_FOUND_REQUEST(404, "Not found request"),
  USER_NOT_FOUND(404, "user not found"),
  ARTICLE_NOT_FOUND(404, "article not found");

  private final int statusCode;
  private final String message;
}
