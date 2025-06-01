package com.inghubs.challenge.mappers.customer;

import com.inghubs.challenge.entities.CustomerEntity;
import com.inghubs.challenge.services.models.customer.CustomerModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {
  CustomerModel toModel(CustomerEntity entity);

  CustomerEntity toEntity(CustomerModel model);
}
