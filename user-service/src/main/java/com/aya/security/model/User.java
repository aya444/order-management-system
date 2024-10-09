package com.aya.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user") // changed entity name because this is a preserved table name in postgresql
public class User implements UserDetails {

    @Id
    @GeneratedValue
    // default value is "strategy = GenerationType.AUTO"; AUTO, detects the best strategy for the DB used
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String pass; // cannot be named password as User of UserDetails interface has a password attribute
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return pass;
    }

}
