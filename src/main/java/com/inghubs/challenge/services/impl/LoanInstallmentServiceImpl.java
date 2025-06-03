package com.inghubs.challenge.services.impl;

import com.inghubs.challenge.entities.LoanInstallmentEntity;
import com.inghubs.challenge.mappers.loanInstallment.LoanInstallmentEntityMapper;
import com.inghubs.challenge.repositories.LoanInstallmentRepository;
import com.inghubs.challenge.services.CustomerService;
import com.inghubs.challenge.services.LoanInstallmentService;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.models.loan.LoanModel;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import com.inghubs.challenge.services.models.loanInstallment.PaymentResultModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.inghubs.challenge.utils.Constants.DISCOUNT_RATE;
import static com.inghubs.challenge.utils.Constants.MAX_ALLOWING_PAY_INSTALLMENT;
import static com.inghubs.challenge.utils.Constants.PENALTY_RATE;

@Service
@RequiredArgsConstructor
public class LoanInstallmentServiceImpl implements LoanInstallmentService {
  private final LoanInstallmentRepository repository;
  private final LoanInstallmentEntityMapper mapper;
  private final CustomerService customerService;
  private final LoanService loanService;

  public List<LoanInstallmentModel> getInstallmentsByLoan(Long loanId) {
    return repository.findInstallmentsByLoan(loanId).stream().map(mapper::toModel).toList();
  }

  @Override
  public PaymentResultModel payInstallments(Long loanId, double amount) {
    LoanModel loan = loanService.getLoanById(loanId);

    LocalDate today = LocalDate.now();
    LocalDate firstOfThisMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    LocalDate lastAllowedDue = firstOfThisMonth.plusMonths(MAX_ALLOWING_PAY_INSTALLMENT);

    List<LoanInstallmentEntity> eligibleInstallments = repository.findEligibleInstallments(loanId, lastAllowedDue);

    int paidCount = 0;
    double totalSpent = 0;

    for (LoanInstallmentEntity installment : eligibleInstallments) {
      double installmentAmount = installment.getAmount();
      LocalDate dueDate = installment.getDueDate();
      long daysDiff = ChronoUnit.DAYS.between(today, dueDate);

      double finalAmount;

      if (daysDiff > 0) {
        double discount = installmentAmount * DISCOUNT_RATE * daysDiff;
        finalAmount = installmentAmount - discount;
      } else if (daysDiff < 0) {
        long daysLate = Math.abs(daysDiff);
        double penalty = installmentAmount * PENALTY_RATE * daysLate;
        finalAmount = installmentAmount + penalty;
      } else {
        finalAmount = installmentAmount;
      }


      if (amount >= installmentAmount) {
        installment.payInstallment(finalAmount);
        repository.save(installment);

        amount -= finalAmount;
        paidCount++;
        totalSpent += finalAmount;
      } else {
        break;
      }
    }

    boolean fullyPaid = repository.isFullyPaidInstallments(loanId);

    if (fullyPaid) {
      loanService.markAsPaid(loanId);
      customerService.decreaseUsedLimit(loan.getCustomer().getId(), loan.getLoanAmount());
    }

    return PaymentResultModel.of(paidCount, totalSpent, fullyPaid);
  }

  @Override
  public void createInstallment(LoanInstallmentModel installment) {
    LoanInstallmentEntity loanInstallment = mapper.toEntity(installment);
    repository.save(loanInstallment);
  }
}
