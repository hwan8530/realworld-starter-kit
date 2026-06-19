package com.realworld.backend.common.errorhandling.handler;

import com.realworld.backend.common.errorhandling.dto.ResponseError;
import com.realworld.backend.common.errorhandling.dto.ResponseError.ErrorDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ResponseError> handleCustomException(CustomException e) {
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add(e.getException().getMessage());
    log.error("getException.getMessage: {}", e.getException().getMessage());
    return ResponseEntity.status(e.getException().getStatusCode())
        .body(new ResponseError(new ErrorDetails(errorMessages)));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException e) {
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ResponseError(new ErrorDetails(errorMessages)));
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<ResponseError> handleOptimisticLockingFailureException(
      OptimisticLockingFailureException e) {
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ResponseError(new ErrorDetails(errorMessages)));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseError> handleException(Exception e) {
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ResponseError(new ErrorDetails(errorMessages)));
  }
}
