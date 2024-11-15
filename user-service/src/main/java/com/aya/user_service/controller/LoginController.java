package com.aya.user_service.controller;

import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.LogInRequest;
import com.aya.user_service.service.LoginService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @NotNull LogInRequest request) {
        AuthenticationResponse authenticationResponse = loginService.login(request);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

}
