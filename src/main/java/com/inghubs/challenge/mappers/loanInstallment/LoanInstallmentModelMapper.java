package com.inghubs.challenge.mappers.loanInstallment;

import com.inghubs.challenge.dtos.installment.response.LoanInstallmentResponse;
import com.inghubs.challenge.dtos.installment.response.PaymentResultResponse;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import com.inghubs.challenge.services.models.loanInstallment.PaymentResultModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanInstallmentModelMapper {

  LoanInstallmentResponse toDto(LoanInstallmentModel model);

  List<LoanInstallmentResponse> toDtos(List<LoanInstallmentModel> models);

  PaymentResultResponse toDto(PaymentResultModel model);
}
