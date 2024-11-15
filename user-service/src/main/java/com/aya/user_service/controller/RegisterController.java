package com.aya.user_service.controller;

import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.RegisterRequest;
import com.aya.user_service.service.RegisterService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;


    // Register as a Customer or Seller
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @NotNull RegisterRequest request) {
        AuthenticationResponse authenticationResponse = registerService.register(request);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
