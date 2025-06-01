package com.inghubs.challenge.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "loan")
public class LoanEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double loanAmount;
  private Integer numberOfInstallment;
  private LocalDate createDate;
  private Boolean isPaid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerEntity customer;

  public static LoanEntity of(Long customerId, Double loanAmount, Integer numberOfInstallment) {
    return LoanEntity.builder()
        .customer(CustomerEntity.of(customerId))
        .loanAmount(loanAmount)
        .numberOfInstallment(numberOfInstallment)
        .createDate(LocalDate.now())
        .isPaid(false)
        .build();
  }
}
