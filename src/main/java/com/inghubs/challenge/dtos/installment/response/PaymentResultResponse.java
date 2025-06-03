package com.inghubs.challenge.dtos.installment.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResultResponse {
  private int installmentsPaid;
  private Double totalAmountSpent;
  private Boolean isLoanPaid;

  public static PaymentResultResponse of(int installmentsPaid, Double totalAmountSpent, Boolean isLoanPaid){
    return PaymentResultResponse.builder()
        .installmentsPaid(installmentsPaid)
        .totalAmountSpent(totalAmountSpent)
        .isLoanPaid(isLoanPaid)
        .build();
  }
}
