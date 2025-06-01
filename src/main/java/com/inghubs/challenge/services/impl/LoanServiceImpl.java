package com.inghubs.challenge.services.impl;

import com.inghubs.challenge.entities.LoanEntity;
import com.inghubs.challenge.exceptions.ApiExceptionType;
import com.inghubs.challenge.exceptions.IngHubsException;
import com.inghubs.challenge.mappers.loan.LoanEntityMapper;
import com.inghubs.challenge.repositories.LoanRepository;
import com.inghubs.challenge.services.CustomerService;
import com.inghubs.challenge.services.LoanInstallmentService;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.models.loan.LoanCreateModel;
import com.inghubs.challenge.services.models.loan.LoanModel;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.inghubs.challenge.utils.Constants.ALLOWED_INSTALLMENTS;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
  private final CustomerService customerService;
  private final LoanRepository repository;
  private final LoanEntityMapper mapper;

  @Lazy
  @Autowired
  private LoanInstallmentService installmentService;


  @Override
  public LoanModel createLoan(Long customerId, LoanCreateModel loan) {
    if(!loan.isAllowingInstallment()){
      throw new IllegalArgumentException("Installment must be one of: " + ALLOWED_INSTALLMENTS);
    }

    if(!loan.isAllowingInterestRate()){
      throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5");
    }

    if(!customerService.hasEnoughCreditLimit(customerId, loan.getAmount())){
      throw new IllegalArgumentException("Not enough credit limit");
    }

    double totalPayable = loan.getAmount() * (1 + loan.getInterestRate());
    double perInstallment = Math.round((totalPayable / loan.getNumberOfInstallment()) * 100.0) / 100.0;

    LoanEntity newLoan = LoanEntity.of(customerId,totalPayable,loan.getNumberOfInstallment());
    repository.save(newLoan);

    LocalDate firstDue = LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    for (int i = 0; i < loan.getNumberOfInstallment(); i++) {
      installmentService.createInstallment(LoanInstallmentModel.newInstallment(perInstallment,firstDue.plusMonths(i), newLoan.getId()));
    }

    customerService.increaseUsedLimit(customerId, loan.getAmount());

    return mapper.toModel(newLoan);
  }

  @Override
  public List<LoanModel> getLoansByCustomer(Long customerId) {
    return repository.findLoanSummariesByCustomerId(customerId).stream().map(mapper::toModel).toList();
  }

  @Override
  public LoanModel getLoanById(Long loanId) {
    return repository.findLoanById(loanId).map(mapper::toModel)
        .orElseThrow(() -> new IngHubsException(ApiExceptionType.LOAN_NOT_FOUND_EXCEPTION, loanId.toString()));
  }

  @Override
  @Transactional
  public void markAsPaid(Long loanId) {
    repository.maskAsPaid(loanId);
  }
}
