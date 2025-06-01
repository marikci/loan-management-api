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
@Table(name = "loan_installment")
public class LoanInstallmentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double amount;
  private Double paidAmount;
  private LocalDate dueDate;
  private LocalDate paymentDate;
  private Boolean isPaid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_id", nullable = false)
  private LoanEntity loan;

  public void payInstallment(Double paidAmount){
    this.paidAmount=paidAmount;
    this.isPaid=true;
    this.paymentDate=LocalDate.now();
  }
}
