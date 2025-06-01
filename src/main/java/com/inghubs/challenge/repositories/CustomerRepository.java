package com.inghubs.challenge.repositories;

import com.inghubs.challenge.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  @Modifying
  @Query("update CustomerEntity c set c.creditLimit=:creditLimit where c.id=:customerId")
  int updateUsedLimit(long customerId, double creditLimit);
}
