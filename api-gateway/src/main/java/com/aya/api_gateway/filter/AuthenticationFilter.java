package com.aya.api_gateway.filter;

import com.aya.api_gateway.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

// AbstractGatewayFilterFactory is a base class provided by Spring Cloud Gateway that helps in creating custom filters
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    // Used to check whether a route is secured and requires authentication.
    @Autowired
    private RouteValidator validator;

    @Autowired
    private AuthoritiesManager authoritiesManager;


    @Autowired
    private JwtUtils jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) { // checks if the incoming request is for a secured route (i.e., it requires authentication).
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) { // checks if the Authorization header is present
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0); // This retrieves the Authorization header value.
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {

                    jwtUtil.validateToken(authHeader);


                    // decode JWT authHeader & extract all claims from token
                    Claims tokenClaims = jwtUtil.extractAllClaims(authHeader);
                    System.out.println("\n" + "\n" + tokenClaims.toString() + "\n" + "\n");

                    // get "role" claim
                    String userRole = tokenClaims.get("role", String.class);
                    System.out.println("\n" + "\n" + "Role: " + userRole + "\n" + "\n");

                    // get URL from request
                    String url = exchange.getRequest().getURI().getPath();
                    System.out.println("\n" + "\n" + "URL: " + url + "\n" + "\n");

                    // check if user has the proper role for the given URL -> implement helper class
                    if(!authoritiesManager.isUserAuthorized(url,userRole)){
                        System.out.println("\n" + "\n" + "Reached authority check and failed!!");
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.setComplete();
                    }


                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("An error has occurred while trying to authenticate and authorize the request");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}