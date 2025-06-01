package com.inghubs.challenge.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IngHubsException.class)
  public ResponseEntity<ErrorResponse> handleApiException(IngHubsException ex, HttpServletRequest request) {

    ApiExceptionType type = ex.getType();
    ErrorResponse body = new ErrorResponse(
        LocalDateTime.now(),
        type.getHttpStatus().value(),
        type.getHttpStatus().getReasonPhrase(),
        type.getCode(),
        type.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(type.getHttpStatus())
        .body(body);
  }
}