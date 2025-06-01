package com.inghubs.challenge.controllers;

import com.inghubs.challenge.dto.installment.response.LoanInstallmentResponse;
import com.inghubs.challenge.dto.installment.response.PaymentResultResponse;
import com.inghubs.challenge.mappers.loanInstallment.LoanInstallmentModelMapper;
import com.inghubs.challenge.services.LoanInstallmentService;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import com.inghubs.challenge.services.models.loanInstallment.PaymentResultModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan-installments")
public class LoanInstallmentController {
  private final LoanInstallmentService service;
  private final LoanService loanService;
  private final LoanInstallmentModelMapper mapper;

  @GetMapping("/loans/{loanId}")
  public ResponseEntity<List<LoanInstallmentResponse>> listInstallments(@PathVariable("loanId") Long loanId) {
    List<LoanInstallmentModel> installments = service.getInstallmentsByLoan(loanId);
    return ResponseEntity.ok(mapper.toDtos(installments));
  }

  @PostMapping("/loans/{loanId}/pay")
  public ResponseEntity<PaymentResultResponse> payInstallments(@PathVariable("loanId") Long loanId, @RequestParam("amount") double amount) throws Exception {
    PaymentResultModel result = service.payInstallments(loanId, amount);
    return ResponseEntity.ok(mapper.toDto(result));
  }
}

