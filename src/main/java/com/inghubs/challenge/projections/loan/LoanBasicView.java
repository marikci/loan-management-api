package com.inghubs.challenge.projections.loan;
import java.time.LocalDate;

public interface LoanBasicView {
  Long getId();
  Double getLoanAmount();
  Integer getNumberOfInstallment();
  LocalDate getCreateDate();
  Boolean getIsPaid();
}
