package com.aya.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JWTService {

    private static final String SECRET_KEY = "5839f4d09d314e3f0ed146f6d9e38a1ed3c4545cc1aaeb6be737a53e47f99826";

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token); // Extract all claims from the token
        return claims.getSubject();  // Return the subject (username) from the claims
        // these lines are substitution of the previous code
        // return extractClaim(token, claims -> claims.getSubject());
        // return extractClaim(token, Claims::getSubject);
    }

    // Used to retrieve any information from a JWT token in a flexible way
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Function takes Claims and produce generic data of type T
        final Claims claims = extractAllClaims(token); // get all claims
        return claimsResolver.apply(claims); // extract the specific data wanted from the claims
    }


    // To decode and extract information from the jwtToken
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser() // Creates a new instance of JwtParserBuilder, which allows to set various properties for parsing JWTs.
                    .setSigningKey(getSignInKey()) // then it Checks the Signature to ensure that the token hasn't been tampered with by using a secret signing key (getSignInKey() method) to verify the token's signature
                    .build() // finalizes the configuration of the JWT parser.
                    .parseClaimsJws(token) // takes the JWT token and parse it to JWS, to validate its signature with the one just set and ensure that is not invalid or expired
                    .getBody();  // Extracts the payload data from the token
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token is invalid", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Token signature does not match", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token is empty or null", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
