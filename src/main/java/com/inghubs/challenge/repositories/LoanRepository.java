package com.inghubs.challenge.repositories;

import com.inghubs.challenge.entities.LoanEntity;
import com.inghubs.challenge.projections.loan.LoanBasicView;
import com.inghubs.challenge.projections.loan.LoanView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

  @Query("""
      select 
        l.id as id,
        l.loanAmount as loanAmount,
        l.numberOfInstallment as numberOfInstallment,
        l.createDate as createDate,
        l.isPaid as isPaid
       from LoanEntity l 
       join l.customer c
       where c.id=:customerId
      """)
  List<LoanBasicView> findLoanSummariesByCustomerId(Long customerId);

  @Query("""
      select 
        l.id as id,
        l.loanAmount as loanAmount,
        l.numberOfInstallment as numberOfInstallment,
        l.createDate as createDate,
        l.isPaid as isPaid,
        c.id as customerId
       from LoanEntity l 
       join l.customer c
       where l.id=:id
      """)
  Optional<LoanView> findLoanById(Long id);

  @Modifying
  @Query("""
      update LoanEntity l set l.isPaid=true where l.id=:loanId
      """)
  void maskAsPaid(Long loanId);
}
