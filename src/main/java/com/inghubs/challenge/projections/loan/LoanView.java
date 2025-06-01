package com.inghubs.challenge.projections.loan;
import java.time.LocalDate;

public interface LoanView {
  Long getId();
  Double getLoanAmount();
  Integer getNumberOfInstallment();
  LocalDate getCreateDate();
  Boolean getIsPaid();
  Long getCustomerId();
}
