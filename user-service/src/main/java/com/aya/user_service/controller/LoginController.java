package com.aya.user_service.controller;

import com.aya.user_service.reqres.LogInRequest;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LogInRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }
}
