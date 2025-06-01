package com.inghubs.challenge.services.models.loanInstallment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PaymentResultModel {
  private int installmentsPaid;
  private Double totalAmountSpent;
  private Boolean isLoanPaid;

  public static PaymentResultModel of(int installmentsPaid,Double totalAmountSpent,Boolean isLoanPaid){
    return PaymentResultModel.builder()
        .installmentsPaid(installmentsPaid)
        .totalAmountSpent(totalAmountSpent)
        .isLoanPaid(isLoanPaid)
        .build();
  }
}
