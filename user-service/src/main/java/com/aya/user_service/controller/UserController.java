package com.aya.user_service.controller;

import com.aya.user_service.reqres.UserDto;
import com.aya.user_service.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/debug")
    public ResponseEntity<String> debugRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return ResponseEntity.ok("Roles: " + auth.getAuthorities());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication found");
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUserAccount(@RequestBody @NotNull UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>("User with role " + userDto.getRole() + " has been created", HttpStatus.OK);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestBody @NotNull String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") @NotNull Integer id) {
        UserDto userDto = userService.getUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-email-by-id/{id}")
    public ResponseEntity<String> getCustomerEmailById(@PathVariable("id") @NotNull Integer id) {
        String email = userService.getCustomerById(id);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUserById(@RequestBody @NotNull String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>("User has been deleted", HttpStatus.OK);
    }
}
