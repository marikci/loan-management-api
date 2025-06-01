package com.inghubs.challenge.repositories;

import com.inghubs.challenge.entities.LoanInstallmentEntity;
import com.inghubs.challenge.projections.installment.LoanInstallmentBasicView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallmentEntity, Long> {

  @Query("""
      select 
        li.id as id,
        li.amount as amount,
        li.paidAmount as paidAmount,
        li.dueDate as dueDate,
        li.paymentDate as paymentDate,
        li.isPaid as isPaid
      from LoanInstallmentEntity li
      join li.loan l
      where l.id=:loanId
      """)
  List<LoanInstallmentBasicView> findInstallmentsByLoan(Long loanId);

  @Query("""
      select li from LoanInstallmentEntity li
      where li.loan.id=:loanId and li.isPaid=false and li.dueDate <= :lastAllowedDue
      order by li.dueDate
      """)
  List<LoanInstallmentEntity> findEligibleInstallments(Long loanId, LocalDate lastAllowedDue);

  @Query("""
      select count(li)=0 from LoanInstallmentEntity li
      where li.loan.id=:loanId and li.isPaid=false
      """)
  Boolean isFullyPaidInstallments(Long loanId);
}
