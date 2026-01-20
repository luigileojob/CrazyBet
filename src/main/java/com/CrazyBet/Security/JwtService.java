package com.CrazyBet.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long TOKEN_VALIDITY = 1000 * 60 * 30; // 30 minuti

    // restituisce chiave jwt
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // genera token con email e ruolo
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // estrae email dal token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // estrae ruolo dal token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // estrae claim dal token
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return resolver.apply(claims);
        } catch (JwtException e) {
            return null; // token non valido o scaduto
        }
    }

    // controllo validità token
    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return username != null && username.equals(email) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token, String email, String role) {
        final String username = extractUsername(token);
        final String tokenRole = extractRole(token);
        return username != null
                && username.equals(email)
                && role.equals(tokenRole)
                && !isTokenExpired(token);
    }

    // controlla se il token è scaduto
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration == null || expiration.before(new Date());
    }

}
