package com.inghubs.challenge.projections.installment;

import java.time.LocalDate;

public interface LoanInstallmentBasicView {
  Long getId();
  Double getAmount();
  Double getPaidAmount();
  LocalDate getDueDate();
  LocalDate getPaymentDate();
  Boolean getIsPaid();
}
