package com.inghubs.challenge.dtos.installment.response;

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

  public static LoanInstallmentResponse of(Long id, Double amount, LocalDate dueDate, Boolean isPaid){
    return LoanInstallmentResponse.builder()
        .id(id)
        .amount(amount)
        .dueDate(dueDate)
        .isPaid(isPaid)
        .build();
  }
}
