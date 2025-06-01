package com.inghubs.challenge.dto.loan.response;

import com.inghubs.challenge.dto.customer.CustomerBaseDto;
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
}
