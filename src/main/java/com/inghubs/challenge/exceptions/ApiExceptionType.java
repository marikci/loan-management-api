package com.inghubs.challenge.exceptions;

import com.inghubs.challenge.utils.Messages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static java.util.Objects.nonNull;

public enum ApiExceptionType {
  CUSTOMER_NOT_FOUND_EXCEPTION(1, "exception.customer.not_found", HttpStatus.BAD_REQUEST),
  CUSTOMER_UPDATE_LIMIT_EXCEPTION(2, "exception.customer.update_limit_problem", HttpStatus.BAD_REQUEST),
  LOAN_NOT_FOUND_EXCEPTION(3, "exception.loan.not_found", HttpStatus.BAD_REQUEST);

  @Getter
  private final int code;
  private final String message;
  @Getter
  private final HttpStatus httpStatus;
  @Setter
  private String[] parameters;

  ApiExceptionType(int code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    if (nonNull(parameters)) {
      return Messages.getMessageForLocale(message, parameters);
    }

    return Messages.getMessageForLocale(message);
  }
}
