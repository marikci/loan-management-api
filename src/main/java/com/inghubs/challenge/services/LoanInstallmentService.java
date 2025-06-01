package com.inghubs.challenge.services;

import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import com.inghubs.challenge.services.models.loanInstallment.PaymentResultModel;

import java.util.List;

public interface LoanInstallmentService {
  List<LoanInstallmentModel> getInstallmentsByLoan(Long loanId);
  PaymentResultModel payInstallments(Long loanId, double amount);
  void createInstallment(LoanInstallmentModel installment);
}
