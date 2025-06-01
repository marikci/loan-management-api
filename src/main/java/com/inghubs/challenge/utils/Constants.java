package com.inghubs.challenge.utils;

import java.util.List;

public class Constants {
  public static final List<Integer> ALLOWED_INSTALLMENTS = List.of(6, 9, 12, 24);
  public static final double ALLOWING_MIN_INTEREST_RATE = 0.1;
  public static final double ALLOWING_MAX_INTEREST_RATE = 0.5;
  public static final int MAX_ALLOWING_PAY_INSTALLMENT = 2;
}
