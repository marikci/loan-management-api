package com.inghubs.challenge.controllers;

import com.inghubs.challenge.configs.JwtUtil;
import com.inghubs.challenge.dtos.auth.request.AuthRequest;
import com.inghubs.challenge.dtos.auth.response.AuthResponse;
import com.inghubs.challenge.services.UserService;
import com.inghubs.challenge.services.models.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    UserModel user = userService.findByUsername(request.getUsername());
    String token = jwtUtil.generateToken(user);

    return ResponseEntity.ok(new AuthResponse(token));
  }
}
