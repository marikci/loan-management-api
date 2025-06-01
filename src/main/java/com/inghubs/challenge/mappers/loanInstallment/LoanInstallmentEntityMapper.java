package com.inghubs.challenge.mappers.loanInstallment;

import com.inghubs.challenge.entities.LoanInstallmentEntity;
import com.inghubs.challenge.projections.installment.LoanInstallmentBasicView;
import com.inghubs.challenge.services.models.loanInstallment.LoanInstallmentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanInstallmentEntityMapper {

  LoanInstallmentModel toModel(LoanInstallmentBasicView view);

  LoanInstallmentEntity toEntity(LoanInstallmentModel model);
}
