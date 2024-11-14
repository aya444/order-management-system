package com.aya.user_service.service;

import com.aya.user_service.exception.InvalidUserDataException;
import com.aya.user_service.exception.NoUserFoundException;
import com.aya.user_service.mapper.UserMapper;
import com.aya.user_service.model.Role;
import com.aya.user_service.model.User;
import com.aya.user_service.repository.UserRepository;
import com.aya.user_service.reqres.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found!";

    public void createUser(UserDto userDto) {
        if (userDto == null) {
            throw new InvalidUserDataException("User data cannot be null!");
        }

        User user = userMapper.fromDtoToUser(userDto);
        userRepository.save(user);
    }

    public UserDto getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidUserDataException("Email cannot be null!");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return userMapper.fromUserToDto(user.get());
        } else {
            throw new NoUserFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public UserDto getUserById(int id) {
        if (id == 0) {
            throw new InvalidUserDataException("User id cannot be null!");
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.fromUserToDto(user.get());
        } else {
            throw new NoUserFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public String getCustomerById(int id) {
        if (id == 0) {
            throw new InvalidUserDataException("User id cannot be null!");
        }

        Optional<User> user = userRepository.findByIdAndRole(id, Role.CUSTOMER);
        if (user.isPresent()) {
           return user.get().getEmail();
        } else {
            throw new NoUserFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public List<UserDto> getAllUsers() {
        Optional<List<User>> users = Optional.of(userRepository.findAll());
        return users.map(userList ->
                        userList.stream()
                                .map(userMapper::fromUserToDto)
                                .toList())
                .orElseGet(List::of);
    }

    public void deleteUser(String email) {
        if (email == null) {
            throw new InvalidUserDataException("Email cannot be null!");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new NoUserFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

}
