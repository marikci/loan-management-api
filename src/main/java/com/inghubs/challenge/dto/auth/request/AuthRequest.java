package com.inghubs.challenge.dto.auth.request;

import lombok.Data;

@Data
public class AuthRequest {
  private String username;
  private String password;
}