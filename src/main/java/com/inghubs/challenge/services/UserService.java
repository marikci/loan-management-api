package com.inghubs.challenge.services;

import com.inghubs.challenge.services.models.user.UserModel;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
  UserModel findByUsername(String username);
  UserDetails loadUserByUsername(String username);
}
