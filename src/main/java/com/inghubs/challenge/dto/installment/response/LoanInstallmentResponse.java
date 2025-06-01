package com.inghubs.challenge.dto.installment.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LoanInstallmentResponse {
  private Long id;
  private Double amount;
  private Double paidAmount;
  private LocalDate dueDate;
  private LocalDate paymentDate;
  private Boolean isPaid;
}
