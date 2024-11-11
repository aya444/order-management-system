package com.aya.user_service.controller;

import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.UserDto;
import com.aya.user_service.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // Register as a Customer or Seller
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto request) {
        return ResponseEntity.ok(registerService.register(request));
    }
}
