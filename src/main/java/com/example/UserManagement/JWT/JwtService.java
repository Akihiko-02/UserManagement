package com.example.UserManagement.JWT;

import com.example.UserManagement.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long Jwtexpiration;

    public Long extractUserid(String jwtToken) {
        String userIdStr = extractClaim(jwtToken, claims-> claims.get("userId",String.class));
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private<T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(User user) {
        return generateToken(new HashMap(),user);
    }
    public String generateToken(Map<String, Object> extractClaims, User user) {
        Map<String, Object> claims = new HashMap(extractClaims);

        claims.put("userId", user.getId().toString());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ Jwtexpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String jwtToken, User user) {
        final Long UserIdfromToken = extractUserid(jwtToken);

        final Long userIdDetails = user.getId();

        return(UserIdfromToken != null && UserIdfromToken.equals(userIdDetails) && !isTokenExpired(jwtToken));
    }

    public boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

}

