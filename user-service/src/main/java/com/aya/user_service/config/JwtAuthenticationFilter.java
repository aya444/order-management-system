package com.aya.user_service.config;

import com.aya.user_service.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    private static final List<String> openEndpoints = List.of(
            "/user/test",
            "/user/register",
            "/user/login"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestPath = request.getServletPath(); // Get the request path
        System.out.println("Request Path: " + requestPath);

        // Skip token validation for open endpoints
        if (openEndpoints.stream().anyMatch(requestPath::equals)) {
            System.out.println("Bypassing JWT validation for open endpoint: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // Skip processing if no token is found
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header or invalid format. Skipping JWT validation.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String username;

        try {
            username = jwtUtils.extractUsername(jwt);
            System.out.println("Extracted Username: " + username);
        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }



//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        final String authenticationHeader = request.getHeader("Authorization");
//        final String jwtToken;
//        final String userEmail;
//
//        // check if JWT token is found
//        if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        // extract JWT token
//        jwtToken = authenticationHeader.substring(7);
//
//        // extract userEmail
//        userEmail = jwtService.extractUsername(jwtToken);
//
//        // validate user
//        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
//
//            // get spring user from DB
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//            if(jwtService.isTokenValid(jwtToken, userDetails)){
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource()
//                                .buildDetails(request)
//                );
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}

//@Component // to make it a spring bean
//@RequiredArgsConstructor
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtUtils jwtUtils;
//    private final UserDetailsService userDetailsService;
//
//    /*
//     * Spring's @NonNullApi annotation, applies @NonNull by default to all method parameters in a package or class,
//     * meaning that all parameters are implicitly expected to be non-null unless otherwise specified.
//     */
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization"); // Bearer Token
//        final String jwtToken;
//
//        // Check if the jwtToken is found
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        // Extract the jwtToken
//        jwtToken = authHeader.substring(7);
//
//        // Extract the username from the jwtToken
//        String userEmail = jwtUtils.extractUsername(jwtToken);
//
//        // Validate user, check if userEmail is not null AND user is not authenticated yet
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
//                // Extract roles from the JWT token
//                List<String> roles = jwtUtils.extractRoles(jwtToken);
//
//                // Convert roles to GrantedAuthority
//                List<GrantedAuthority> authorities = roles.stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null, // bec. we don't have credentials when we made the user
//                        authorities
//                );
//                authenticationToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
