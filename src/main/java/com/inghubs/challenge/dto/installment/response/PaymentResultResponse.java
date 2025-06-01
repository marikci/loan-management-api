package com.inghubs.challenge.dto.installment.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResultResponse {
  private int installmentsPaid;
  private Double totalAmountSpent;
  private Boolean isLoanPaid;
}
