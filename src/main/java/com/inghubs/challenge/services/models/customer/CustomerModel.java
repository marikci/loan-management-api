package com.inghubs.challenge.services.models.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModel {
  private Long id;
  private String name;
  private String surname;
  private Double creditLimit;
  private Double usedCreditLimit;

}
