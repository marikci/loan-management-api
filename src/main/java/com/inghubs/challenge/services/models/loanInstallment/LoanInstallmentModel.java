package com.inghubs.challenge.services.models.loanInstallment;

import com.inghubs.challenge.services.models.loan.LoanModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanInstallmentModel {
  private Long id;
  private Double amount;
  private Double paidAmount;
  private LocalDate dueDate;
  private LocalDate paymentDate;
  private Boolean isPaid;
  private LoanModel loan;

  public static LoanInstallmentModel newInstallment(Double amount, LocalDate dueDate, Long loanId){
    return LoanInstallmentModel.builder()
        .loan(LoanModel.of(loanId))
        .amount(amount)
        .dueDate(dueDate)
        .isPaid(false)
        .build();
  }
}
