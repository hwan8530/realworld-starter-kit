package com.realworld.backend.errorhandling.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
  private final CustomExceptionList exception;
}
