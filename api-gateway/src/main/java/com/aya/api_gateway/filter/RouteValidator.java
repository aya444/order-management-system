package com.aya.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // List of endpoints that do not require authentication
    public static final List<String> openApiEndpoints = List.of(
            "/user/test",
            "/user/register",
            "/user/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        boolean isSecured = openApiEndpoints.stream()
                .noneMatch(uri -> path.startsWith(uri));
        System.out.println("Request path: " + path + ", isSecured: " + isSecured);
        return isSecured;
    };

}