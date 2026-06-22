package com.realworld.backend.common.errorhandling.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
  private final CustomExceptionList exception;
}
