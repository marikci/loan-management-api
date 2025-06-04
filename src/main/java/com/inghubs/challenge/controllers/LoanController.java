package com.inghubs.challenge.controllers;

import com.inghubs.challenge.dtos.loan.request.LoanCreateRequest;
import com.inghubs.challenge.dtos.loan.response.LoanResponse;
import com.inghubs.challenge.mappers.loan.LoanModelMapper;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.models.loan.LoanModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loans")
public class LoanController {
  private final LoanService loanService;
  private final LoanModelMapper mapper;

  @PostMapping("/customers/{customerId}/loans")
  @PreAuthorize("hasRole('ADMIN') or #customerId == principal.id")
  public ResponseEntity<LoanResponse> createLoan(@PathVariable("customerId") Long customerId, @RequestBody @Valid LoanCreateRequest request) {
    LoanModel loan = loanService.createLoan(customerId, mapper.toModel(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(loan));
  }

  @GetMapping("/customers/{customerId}")
  @PreAuthorize("hasRole('ADMIN') or #customerId == principal.id")
  public ResponseEntity<List<LoanResponse>> listLoans(@PathVariable("customerId") Long customerId) {
    List<LoanModel> loans = loanService.getLoansByCustomer(customerId);
    return ResponseEntity.ok(mapper.toDtos(loans));
  }


}
