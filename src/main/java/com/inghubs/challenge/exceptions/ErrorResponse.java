package com.inghubs.challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private int code;
  private String message;
  private String path;
}