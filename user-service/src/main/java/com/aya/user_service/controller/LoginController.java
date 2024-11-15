package com.aya.user_service.controller;

import com.aya.user_service.reqres.LogInRequest;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LogInRequest request) throws IllegalAccessException {
        return ResponseEntity.ok(loginService.authenticate(request));
    }
}
