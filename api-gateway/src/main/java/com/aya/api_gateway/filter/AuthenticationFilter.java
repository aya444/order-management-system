package com.aya.api_gateway.filter;

import com.aya.api_gateway.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

// AbstractGatewayFilterFactory is a base class provided by Spring Cloud Gateway that helps in creating custom filters
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator validator;
    private final AuthoritiesManager authoritiesManager;
    private final JwtUtils jwtUtil;

    // Constructor-based dependency injection
    public AuthenticationFilter(RouteValidator validator, AuthoritiesManager authoritiesManager, JwtUtils jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.authoritiesManager = authoritiesManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) { // checks if the incoming request is for a secured route
                try {
                    String authHeader = extractAuthHeader(exchange); // Extract and validate the Authorization header
                    jwtUtil.validateToken(authHeader); // Validate the token

                    Claims tokenClaims = jwtUtil.extractAllClaims(authHeader); // Extract token claims
                    String userRole = extractUserRole(tokenClaims); // Extract the user's role
                    String url = extractRequestUrl(exchange); // Extract the request URL

                    validateUserAuthorization(url, userRole); // Check if the user is authorized for the requested URL

                } catch (Exception e) {
                    System.out.println("Authentication/Authorization failed: " + e.getMessage());
                    return handleUnauthorizedAccess(exchange); // Handle unauthorized access
                }
            }
            return chain.filter(exchange);
        };
    }

    // Extract and validate the Authorization header
    private String extractAuthHeader(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) { // Check if Authorization header is present
            throw new RuntimeException("Missing Authorization header");
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Check if Authorization header is valid
            throw new RuntimeException("Invalid Authorization header format");
        }

        return authHeader.substring(7); // To remove "Bearer " prefix
    }

    // Extract the user's role from the token
    private String extractUserRole(Claims tokenClaims) {
        List<String> roles = tokenClaims.get("roles", List.class);
        if (roles == null || roles.isEmpty()) {
            throw new RuntimeException("No roles found in token");
        }

        String userRole = roles.getFirst(); // Get the first role
        System.out.println("Extracted Role: " + userRole);
        return userRole;
    }

    // Extract the request URL
    private String extractRequestUrl(ServerWebExchange exchange) {
        String url = exchange.getRequest().getURI().getPath();
        System.out.println("Request URL: " + url);
        return url;
    }

    // Check if the user is authorized for the requested URL
    private void validateUserAuthorization(String url, String role) {
        if (!authoritiesManager.isUserAuthorized(url, role)) { // Check if the user is authorized for the requested URL
            System.out.println("Authorization check failed for URL: " + url + " with role: " + role);
            throw new RuntimeException("User not authorized for this URL");
        }
    }

    // Handle unauthorized access
    private Mono<Void> handleUnauthorizedAccess(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {}
}