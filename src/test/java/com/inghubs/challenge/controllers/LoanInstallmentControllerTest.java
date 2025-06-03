package com.inghubs.challenge.controllers;

import com.inghubs.challenge.configs.JwtUtil;
import com.inghubs.challenge.configs.SecurityConfig;
import com.inghubs.challenge.dto.installment.response.LoanInstallmentResponse;
import com.inghubs.challenge.dto.installment.response.PaymentResultResponse;
import com.inghubs.challenge.mappers.loan.LoanModelMapper;
import com.inghubs.challenge.mappers.loanInstallment.LoanInstallmentModelMapper;
import com.inghubs.challenge.services.LoanInstallmentService;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.UserService;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import com.inghubs.challenge.services.models.loanInstallment.PaymentResultModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanInstallmentController.class)
@Import(SecurityConfig.class)
class LoanInstallmentControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private LoanInstallmentService loanInstallmentService;

  @MockBean
  private UserService userService;

  @MockBean
  private LoanService loanService;

  @MockBean
  private LoanInstallmentModelMapper mapper;

  @MockBean
  private LoanModelMapper loanModelMapper;

  @Test
  @DisplayName("listInstallments: When authenticated as ADMIN, returns 200 and valid JSON array")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void listInstallments_AsAdmin_ReturnsOkAndJsonArray() throws Exception {
    // Arrange
    Long loanId = 10L;

    // Simulate two installments in service layer
    LoanInstallmentModel installment1 = LoanInstallmentModel.of(1L,500.0, LocalDate.of(2025, 7, 1), false,loanId);

    LoanInstallmentModel installment2 = LoanInstallmentModel.of(2L,500.0, LocalDate.of(2025, 8, 1), true,loanId);

    List<LoanInstallmentModel> installmentModels = List.of(installment1, installment2);

    // Prepare corresponding response DTOs
    LoanInstallmentResponse response1 = LoanInstallmentResponse.of(1L, 500.0,LocalDate.of(2025, 7, 1), false);
    LoanInstallmentResponse response2 = LoanInstallmentResponse.of(2L, 500.0,LocalDate.of(2025, 8, 1), true);

    List<LoanInstallmentResponse> responseList = List.of(response1, response2);

    given(loanInstallmentService.getInstallmentsByLoan(eq(loanId)))
        .willReturn(installmentModels);
    given(mapper.toDtos(eq(installmentModels)))
        .willReturn(responseList);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loan-installments/loans/{loanId}", loanId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].amount").value(500.0))
        .andExpect(jsonPath("$[0].dueDate").value("2025-07-01"))
        .andExpect(jsonPath("$[0].isPaid").value(false))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].amount").value(500.0))
        .andExpect(jsonPath("$[1].dueDate").value("2025-08-01"))
        .andExpect(jsonPath("$[1].isPaid").value(true));
  }

  @Test
  @DisplayName("payInstallments: When authenticated as ADMIN, returns 200 and correct JSON")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void payInstallments_AsAdmin_ReturnsOkAndJson() throws Exception {
    // Arrange
    Long loanId = 10L;
    double amount = 500.0;

    // Simulate service returning a PaymentResultModel
    PaymentResultModel resultModel = PaymentResultModel.of(1, 500.0, false);

    PaymentResultResponse responseDto = PaymentResultResponse.of(1,500.0,false);

    given(loanInstallmentService.payInstallments(eq(loanId), eq(amount)))
        .willReturn(resultModel);
    given(mapper.toDto(eq(resultModel)))
        .willReturn(responseDto);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loan-installments/loans/{loanId}/pay", loanId)
            .param("amount", String.valueOf(amount))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.installmentsPaid").value(1))
        .andExpect(jsonPath("$.totalAmountSpent").value(500.0))
        .andExpect(jsonPath("$.isLoanPaid").value(false));
  }
}