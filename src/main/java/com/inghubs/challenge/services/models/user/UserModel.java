package com.inghubs.challenge.services.models.user;

import com.inghubs.challenge.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserModel {
  private Long id;

  private String username;

  private String password;

  private Role role;

  private Long customerId;
}