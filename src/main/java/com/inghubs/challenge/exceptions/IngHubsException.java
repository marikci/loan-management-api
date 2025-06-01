package com.inghubs.challenge.exceptions;

import lombok.Getter;

@Getter
public class IngHubsException extends RuntimeException {
  private final ApiExceptionType type;

  public IngHubsException(ApiExceptionType type) {
    super(type.getMessage());
    this.type = type;
  }

  public IngHubsException(ApiExceptionType type, String... parameters) {
    super(type.getMessage());
    type.setParameters(parameters);
    this.type = type;
  }
}
