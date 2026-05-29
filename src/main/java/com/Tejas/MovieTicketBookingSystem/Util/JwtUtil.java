package com.Tejas.MovieTicketBookingSystem.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service

public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public SecretKey getKey() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String email) {
        logger.info("Generating JWT token for user: {}", email);
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        String token = Jwts.builder()
                .claims(Map.of())
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getKey())
                .compact();
        logger.info("Token is generated for user with username: {}", email);
        return token;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaims(String token, Function<Claims, T> ClaimsResolver) {
        Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        String username = extractClaims(token, Claims::getSubject);
        return username;
    }

    public Date extractExpiration(String token) {
        Date expiration = extractClaims(token, Claims::getExpiration);
        return expiration;
    }

    public boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            logger.warn("JWT token has expired");
        }
        return expired;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        logger.info("Validating JWT token for user: {}", userDetails.getUsername());
        boolean expired = isTokenExpired(token);
        if (!expired && extractUsername(token).equals(userDetails.getUsername())) {
            logger.info("JWT token validated successfully for user: {}", userDetails.getUsername());
            return true;
        } else {
            logger.warn("JWT token validation failed for user: {}", userDetails.getUsername());
            return false;
        }
    }
}
