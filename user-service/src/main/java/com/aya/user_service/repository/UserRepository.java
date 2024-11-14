package com.aya.user_service.repository;

import com.aya.user_service.model.Role;
import com.aya.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(int id);

    Optional<User> findByIdAndRole(int id, Role role);
}
