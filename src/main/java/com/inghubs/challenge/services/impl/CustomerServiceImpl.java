package com.inghubs.challenge.services.impl;

import com.inghubs.challenge.exceptions.ApiExceptionType;
import com.inghubs.challenge.exceptions.IngHubsException;
import com.inghubs.challenge.mappers.customer.CustomerEntityMapper;
import com.inghubs.challenge.repositories.CustomerRepository;
import com.inghubs.challenge.services.CustomerService;
import com.inghubs.challenge.services.models.customer.CustomerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final CustomerEntityMapper mapper;

  @Override
  public CustomerModel getCustomerById(Long id) {
    return customerRepository.findById(id)
        .map(mapper::toModel).orElseThrow(()-> new IngHubsException(ApiExceptionType.CUSTOMER_NOT_FOUND_EXCEPTION, id.toString()));
  }

  @Override
  public boolean hasEnoughCreditLimit(Long customerId, double requestedAmount) {
    CustomerModel customer = getCustomerById(customerId);
    double availableLimit = customer.getCreditLimit() - customer.getUsedCreditLimit();
    return requestedAmount <= availableLimit;
  }

  @Override
  @Transactional
  public void increaseUsedLimit(Long customerId, double amount) {
    updateUsedLimit(customerId,amount);
  }

  @Override
  @Transactional
  public void decreaseUsedLimit(Long customerId, double amount) {
    updateUsedLimit(customerId,amount);
  }

  private void updateUsedLimit(Long customerId, double creditLimit) {
    int rowCount = customerRepository.updateUsedLimit(customerId, creditLimit);
    if(rowCount==0){
      throw new IngHubsException(ApiExceptionType.CUSTOMER_UPDATE_LIMIT_EXCEPTION, customerId.toString());
    }
  }
}
