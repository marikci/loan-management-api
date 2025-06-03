package com.inghubs.challenge.services.impl;

import com.inghubs.challenge.exceptions.ApiExceptionType;
import com.inghubs.challenge.exceptions.IngHubsException;
import com.inghubs.challenge.mappers.user.UserEntityMapper;
import com.inghubs.challenge.repositories.UserRepository;
import com.inghubs.challenge.services.UserService;
import com.inghubs.challenge.services.models.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository userRepository;
  private final UserEntityMapper mapper;


  @Override
  public UserModel findByUsername(String username) {
    return userRepository.findByUsername(username).map(mapper::toModel)
        .orElseThrow(() -> new IngHubsException(ApiExceptionType.USER_NOT_FOUND_EXCEPTION, username));
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserModel user = findByUsername(username);

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
    );
  }
}
