package com.inghubs.challenge.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghubs.challenge.configs.JwtUtil;
import com.inghubs.challenge.configs.SecurityConfig;
import com.inghubs.challenge.dto.customer.CustomerBaseDto;
import com.inghubs.challenge.dto.loan.request.LoanCreateRequest;
import com.inghubs.challenge.dto.loan.response.LoanResponse;
import com.inghubs.challenge.mappers.loan.LoanModelMapper;
import com.inghubs.challenge.services.LoanService;
import com.inghubs.challenge.services.UserService;
import com.inghubs.challenge.services.models.customer.CustomerModel;
import com.inghubs.challenge.services.models.loan.LoanCreateModel;
import com.inghubs.challenge.services.models.loan.LoanModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(LoanController.class)
@Import(SecurityConfig.class)
class LoanControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoanService loanService;

  @MockBean
  private LoanModelMapper mapper;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("createLoan: When authenticated as ADMIN with CSRF token, returns 201 and valid JSON")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void createLoan_AsAdmin_WithCsrfAndValidResponse() throws Exception {
    // Arrange
    Long customerId = 42L;

    // Request payload
    LoanCreateRequest requestDto = LoanCreateRequest.of(5000.0, null, 12);

    // Convert request to domain model
    LoanCreateModel createModel = LoanCreateModel.of(5000.0, null,12);

    // Simulate saved loan in service layer
    CustomerModel mockedCustomer = CustomerModel.of(customerId);

    LoanModel savedLoanModel = LoanModel.of(100L,5000.0,12,LocalDate.of(2025, 6, 3), false,mockedCustomer);

    // Prepare response DTO
    CustomerBaseDto mockedCustomerDto = CustomerBaseDto.of(customerId);

    LoanResponse responseDto = LoanResponse.of(100L, 5000.0, 12,LocalDate.of(2025, 6, 3),false, mockedCustomerDto);

    // Mockito stubs
    given(mapper.toModel(any(LoanCreateRequest.class)))
        .willReturn(createModel);

    given(loanService.createLoan(eq(customerId), eq(createModel)))
        .willReturn(savedLoanModel);

    given(mapper.toDto(eq(savedLoanModel)))
        .willReturn(responseDto);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/customers/{customerId}/loans", customerId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(100))
        .andExpect(jsonPath("$.loanAmount").value(5000.0))
        .andExpect(jsonPath("$.numberOfInstallment").value(12))
        .andExpect(jsonPath("$.isPaid").value(false))
        .andExpect(jsonPath("$.customer.id").value(42));
  }

  @Test
  @DisplayName("createLoan: When authenticated as CUSTOMER for own customerId, returns 201 and valid JSON")
  void createLoan_AsCustomerForOwnId_ReturnsCreated() throws Exception {
    // Arrange
    Long customerId = 42L;

    // Prepare request payload
    LoanCreateRequest requestDto = LoanCreateRequest.of(5000.0, null,12);

    // Create principal representing the authenticated customer
    CustomerModel customerPrincipal = CustomerModel.of(customerId);

    // Map request to domain model
    LoanCreateModel createModel = LoanCreateModel.of(5000.0, null, 12);

    // Simulate service returning a saved LoanModel
    LoanModel savedLoanModel = LoanModel.of(100L, 5000.0, 12,LocalDate.of(2025, 6, 3),false, customerPrincipal);

    // Prepare response DTO
    CustomerBaseDto mockedCustomerDto = CustomerBaseDto.of(customerId);

    LoanResponse responseDto = LoanResponse.of(100L, 5000.0,12,LocalDate.of(2025, 6, 3),false,mockedCustomerDto);

    // Mockito stubs
    given(mapper.toModel(any(LoanCreateRequest.class)))
        .willReturn(createModel);

    given(loanService.createLoan(eq(customerId), eq(createModel)))
        .willReturn(savedLoanModel);

    given(mapper.toDto(eq(savedLoanModel)))
        .willReturn(responseDto);

    // Build authentication token for CUSTOMER role
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        customerPrincipal,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
    );

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/customers/{customerId}/loans", customerId)
            .with(authentication(authToken))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(100))
        .andExpect(jsonPath("$.loanAmount").value(5000.0))
        .andExpect(jsonPath("$.numberOfInstallment").value(12))
        .andExpect(jsonPath("$.isPaid").value(false))
        .andExpect(jsonPath("$.customer.id").value(customerId));
  }

  @Test
  @DisplayName("createLoan: When authenticated as CUSTOMER for a different customerId, returns 403 Forbidden")
  void createLoan_AsCustomerForDifferentId_ReturnsForbidden() throws Exception {
    // Arrange
    Long ownId = 42L;
    Long otherId = 99L;

    LoanCreateRequest requestDto = LoanCreateRequest.of(7000.0,null,8);

    CustomerModel customerPrincipal = CustomerModel.of(ownId);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        customerPrincipal,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
    );

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/customers/{customerId}/loans", otherId)
            .with(authentication(authToken))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("listLoans: When authenticated as ADMIN, returns 200 and valid JSON array")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void listLoans_AsAdmin_ReturnsOkAndJsonArray() throws Exception {
    // Arrange
    Long customerId = 42L;

    // Simulate service returning a list of LoanModel
    CustomerModel mockedCustomer = CustomerModel.builder()
        .id(customerId)
        .build();
    LoanModel loan1 = LoanModel.builder()
        .id(100L)
        .loanAmount(5000.0)
        .numberOfInstallment(12)
        .createDate(LocalDate.of(2025, 6, 3))
        .isPaid(false)
        .customer(mockedCustomer)
        .build();
    LoanModel loan2 = LoanModel.builder()
        .id(101L)
        .loanAmount(3000.0)
        .numberOfInstallment(6)
        .createDate(LocalDate.of(2025, 5, 20))
        .isPaid(true)
        .customer(mockedCustomer)
        .build();
    List<LoanModel> loanModels = List.of(loan1, loan2);

    // Prepare corresponding response DTOs
    CustomerBaseDto customerDto = CustomerBaseDto.builder()
        .id(customerId)
        .build();
    LoanResponse response1 = LoanResponse.builder()
        .id(100L)
        .loanAmount(5000.0)
        .numberOfInstallment(12)
        .createDate(LocalDate.of(2025, 6, 3))
        .isPaid(false)
        .customer(customerDto)
        .build();
    LoanResponse response2 = LoanResponse.builder()
        .id(101L)
        .loanAmount(3000.0)
        .numberOfInstallment(6)
        .createDate(LocalDate.of(2025, 5, 20))
        .isPaid(true)
        .customer(customerDto)
        .build();
    List<LoanResponse> responseList = List.of(response1, response2);

    given(loanService.getLoansByCustomer(eq(customerId)))
        .willReturn(loanModels);
    given(mapper.toDtos(eq(loanModels)))
        .willReturn(responseList);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loans/customers/{customerId}", customerId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(100))
        .andExpect(jsonPath("$[0].loanAmount").value(5000.0))
        .andExpect(jsonPath("$[0].numberOfInstallment").value(12))
        .andExpect(jsonPath("$[0].isPaid").value(false))
        .andExpect(jsonPath("$[1].id").value(101))
        .andExpect(jsonPath("$[1].loanAmount").value(3000.0))
        .andExpect(jsonPath("$[1].numberOfInstallment").value(6))
        .andExpect(jsonPath("$[1].isPaid").value(true));
  }

}