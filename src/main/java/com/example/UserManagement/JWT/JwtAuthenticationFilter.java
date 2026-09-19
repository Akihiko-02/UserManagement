package com.example.UserManagement.JWT;

import com.example.UserManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Long userId = null;
        String jwtToken = null;

        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        //if null check cookies
        if(jwtToken == null) {
            Cookie[] cookie = request.getCookies();
            if(cookie != null) {
                for(Cookie c : cookie) {
                    if("JWT".equals(c.getName())) {
                        jwtToken = c.getValue();
                        break;
                    }
                }
            }
        }

        //if no token found
        if(jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract the userID from Jwt token
        userId = jwtService.extractUserid(jwtToken);

        if(userId != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userRepository.findById(userId)
                    .orElseThrow(()-> new RuntimeException("User not found"));
            System.out.println("USER ID FROM TOKEN: " + userId);
            System.out.println("TOKEN VALID: " + jwtService.isTokenValid(jwtToken,userDetails));
            System.out.println("AUTHORITIES: " + userDetails.getRoles());
            if(jwtService.isTokenValid(jwtToken,userDetails)){
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("AUTHENTICATION SET: " +
                        SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

                System.out.println("AUTH USER: " +
                        SecurityContextHolder.getContext().getAuthentication().getName());

                System.out.println("AUTH AUTHORITIES: " +
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }
        }
        filterChain.doFilter(request, response);
    }
}
