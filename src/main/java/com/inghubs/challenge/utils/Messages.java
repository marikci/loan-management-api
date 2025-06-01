package com.inghubs.challenge.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages {

  private static MessageSource messageSource;

  public Messages(MessageSource messageSource) {
    Messages.messageSource = messageSource;
  }

  public static String getMessageForLocale(String key, Object... args) {
    return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
