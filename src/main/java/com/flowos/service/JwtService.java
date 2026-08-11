package com.flowos.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${flowos.jwt.secret}") String secret,
                      @Value("${flowos.jwt.expiration-ms}") long expirationMs){
        this.key= Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs=expirationMs;
    }

    public String generateToken(UUID userId, String role, UUID organizationId){
        Date now=new Date();
        Date expiry=new Date(now.getTime()+expirationMs);

        return Jwts.builder().subject(userId.toString())
                .claims(Map.of("role",role,"orgId",organizationId.toString()))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private io.jsonwebtoken.Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
