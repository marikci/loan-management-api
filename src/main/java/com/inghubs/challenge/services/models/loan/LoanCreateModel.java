package com.inghubs.challenge.services.models.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.inghubs.challenge.utils.Constants.ALLOWED_INSTALLMENTS;
import static com.inghubs.challenge.utils.Constants.ALLOWING_MAX_INTEREST_RATE;
import static com.inghubs.challenge.utils.Constants.ALLOWING_MIN_INTEREST_RATE;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanCreateModel {
  private Double amount;
  private Double interestRate;
  private Integer numberOfInstallment;

  public static LoanCreateModel of(Double amount,Double interestRate, Integer numberOfInstallment){
    return LoanCreateModel.builder()
        .amount(amount)
        .interestRate(interestRate)
        .numberOfInstallment(numberOfInstallment)
        .build();
  }

  public boolean isAllowingInstallment(){
    return ALLOWED_INSTALLMENTS.contains(this.numberOfInstallment);
  }

  public boolean isAllowingInterestRate(){
    return this.interestRate >= ALLOWING_MIN_INTEREST_RATE && this.interestRate <= ALLOWING_MAX_INTEREST_RATE;
  }
}
