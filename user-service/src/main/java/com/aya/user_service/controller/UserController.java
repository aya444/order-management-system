package com.aya.user_service.controller;

import com.aya.user_service.reqres.RegisterRequest;
import com.aya.user_service.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-seller")
    public ResponseEntity<String> createNonCustomerUser(@RequestBody @NotNull RegisterRequest registerRequest) {
        userService.createSellerUser(registerRequest);
        return new ResponseEntity<>("Seller has been created", HttpStatus.OK);
    }

}
