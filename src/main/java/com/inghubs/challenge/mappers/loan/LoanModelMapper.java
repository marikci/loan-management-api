package com.inghubs.challenge.mappers.loan;

import com.inghubs.challenge.dto.loan.request.LoanCreateRequest;
import com.inghubs.challenge.dto.loan.response.LoanResponse;
import com.inghubs.challenge.services.models.loan.LoanCreateModel;
import com.inghubs.challenge.services.models.loan.LoanModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanModelMapper {
  LoanResponse toDto(LoanModel model);

  List<LoanResponse> toDtos(List<LoanModel> models);

  LoanCreateModel toModel(LoanCreateRequest dto);
}
