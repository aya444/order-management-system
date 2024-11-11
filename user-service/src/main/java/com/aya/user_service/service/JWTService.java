package com.aya.user_service.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${JWT_SECRET}")
    private String SECRET_KEY;

    /*
         Key -> is what represent a signature
         1- Get the signature of the SECRET_KEY,
         then later compare it with the signature of the token in extractAllClaims method
    */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
        Claims -> are what represent a user (info),
        2- To decode and extract claims(info) about user from the jwtToken
    */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser() // Creates a new instance of JwtParserBuilder, which allows to set various properties for parsing JWTs.
                    .verifyWith(getSignInKey()) // then it Checks the Signature to ensure that the token hasn't been tampered with by using a secret signing key (getSignInKey() method) to verify the token's signature
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();  // Extracts the payload data from the token
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Token has expired", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Token is invalid", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Token signature does not match", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token is empty or null", e);
        }
    }

    // 3- Used to retrieve any kind of information from a JWT token in a flexible way
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Function takes Claims and produce generic data of type T
        final Claims claims = extractAllClaims(token); // get all claims
        return claimsResolver.apply(claims); // extract the specific data wanted from the claims
    }

    // 4- Extract username from the list of claims(info) about the user
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // getSubject() -> Subject(username) of the JWT (the user)
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Ensure "roles" matches the claim key
    }


    // 5- Generate a JWT token for the user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // Claims are the payload of the token, where we store user-related data such as roles, user ID, or other metadata.

        // Add roles to the claims
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // Ensure ROLE_ prefix
                .toList());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date creationDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 24 hours

        return Jwts.builder()
                .claims(claims)
                .subject(subject) // User's identity
                .issuedAt(creationDate) // When token is created
                .expiration(expirationDate) // Token expiration time
                .signWith(getSignInKey()) // Signing key
                .compact(); // Generate the token
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
