package com.inghubs.challenge.dtos.loan.response;

import com.inghubs.challenge.dtos.customer.CustomerBaseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class LoanResponse {
  private Long id;
  private Double loanAmount;
  private Integer numberOfInstallment;
  private LocalDate createDate;
  private Boolean isPaid;
  private CustomerBaseDto customer;

  public static LoanResponse of(Long id, Double loanAmount, Integer numberOfInstallment, LocalDate createDate, Boolean isPaid, CustomerBaseDto customer){
    return LoanResponse.builder()
        .id(id)
        .loanAmount(loanAmount)
        .numberOfInstallment(numberOfInstallment)
        .createDate(createDate)
        .isPaid(isPaid)
        .customer(customer)
        .build();
  }
}
