package com.inghubs.challenge.services;

import com.inghubs.challenge.services.models.customer.CustomerModel;

public interface CustomerService {
  CustomerModel getCustomerById(Long id);
  boolean hasEnoughCreditLimit(Long customerId, double requestedAmount);
  void increaseUsedLimit(Long customerId, double amount);
  void decreaseUsedLimit(Long customerId, double amount);
}
