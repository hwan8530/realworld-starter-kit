package com.realworld.backend.common.errorhandling.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
  private ErrorDetails errors;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ErrorDetails {
    private List<String> body;
  }
}
