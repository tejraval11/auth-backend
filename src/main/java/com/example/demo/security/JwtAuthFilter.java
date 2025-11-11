package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String path = request.getRequestURI();
            String authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            try{
                String username = jwtService.extractUsername(token);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userService.getUserByUsername(username);

                    if (user != null && jwtService.extractUsername(token).equals(user.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            catch (Exception e){
                System.err.println("[JWT Filter] Token validation failed: " + e.getMessage());
            }
            filterChain.doFilter(request,response);
    }
}
