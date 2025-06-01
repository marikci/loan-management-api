package com.inghubs.challenge.mappers.loan;

import com.inghubs.challenge.entities.LoanEntity;
import com.inghubs.challenge.projections.loan.LoanBasicView;
import com.inghubs.challenge.projections.loan.LoanView;
import com.inghubs.challenge.services.models.loan.LoanModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanEntityMapper {
  LoanModel toModel(LoanEntity entity);

  LoanModel toModel(LoanBasicView view);

  @Mapping(target = "customer.id", source = "customerId")
  LoanModel toModel(LoanView view);
}
