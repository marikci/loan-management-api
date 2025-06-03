package com.inghubs.challenge.services.models.loan;

import com.inghubs.challenge.services.models.customer.CustomerModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanModel {
  private Long id;
  private Double loanAmount;
  private Integer numberOfInstallment;
  private LocalDate createDate;
  private Boolean isPaid;
  private CustomerModel customer;

  public static LoanModel of(Long id){
    return LoanModel.builder()
        .id(id)
        .build();
  }

  public static LoanModel of(Long id, Double loanAmount, Integer numberOfInstallment, LocalDate createDate, Boolean isPaid, CustomerModel customer){
    return LoanModel.builder()
        .id(id)
        .loanAmount(loanAmount)
        .numberOfInstallment(numberOfInstallment)
        .createDate(createDate)
        .isPaid(isPaid)
        .customer(customer)
        .build();
  }
}
