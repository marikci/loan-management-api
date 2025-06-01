package com.inghubs.challenge.dto.loan.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanCreateRequest {
  private Double amount;
  private Double interestRate;
  private Integer numberOfInstallment;
}
