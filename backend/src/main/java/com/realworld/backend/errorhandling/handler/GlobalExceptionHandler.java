package com.realworld.backend.errorhandling.handler;

import com.realworld.backend.errorhandling.dto.ResponseError;
import com.realworld.backend.errorhandling.dto.ResponseError.ErrorDetails;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  public ResponseEntity<ResponseError> handleRuntimeException(CustomException e) {
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add(e.getException().getMessage());
    return ResponseEntity.status(e.getException().getStatusCode()).body(new ResponseError(new ErrorDetails(errorMessages)));
  }
}
