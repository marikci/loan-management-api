package com.inghubs.challenge.dtos.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerBaseDto {
  private Long id;

  public static CustomerBaseDto of(Long id){
    return CustomerBaseDto.builder()
        .id(id)
        .build();
  }
}
