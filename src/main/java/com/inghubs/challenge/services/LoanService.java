package com.inghubs.challenge.services;

import com.inghubs.challenge.services.models.loan.LoanCreateModel;
import com.inghubs.challenge.services.models.loan.LoanModel;

import java.util.List;

public interface LoanService {
  LoanModel createLoan(Long customerId, LoanCreateModel loan);
  List<LoanModel> getLoansByCustomer(Long customerId);
  LoanModel getLoanById(Long loanId);
  void markAsPaid(Long loanId);
}
