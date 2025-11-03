package br.fai.lds.medlink.implementation.service.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Profile("jwt")
@Component()
public class JwtService {
    private final String secret = "bWVkbGlua2p3dHNlY3JldGtleWZvcmF1dGhlbnRpY2F0aW9uYW5kYXV0aG9yaXphdGlvbjIwMjQ=";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secret));

    public String getEmailFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getRoleFromToken(String token){
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    public String getUserIdFromToken(String token){
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }

    public String getFullnameFromToken(String token){
        return getClaimFromToken(token, claims -> claims.get("fullname", String.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean tokenExpired(String token){
        try {
            final Date expirationDate = getExpirationDateFromToken(token);
            boolean expired = expirationDate.before(new Date());
            System.out.println("Token expiration check - Expires: " + expirationDate + ", Now: " + new Date() + ", Expired: " + expired);
            return expired;
        } catch (Exception e) {
            System.out.println("Erro ao verificar expiração do token: " + e.getMessage());
            return true;
        }
    }

    public boolean validadeToken(String token, UserDetails userDetails){
        try {
            final String email = getEmailFromToken(token);
            boolean emailMatches = email.equals(userDetails.getUsername());
            boolean notExpired = !tokenExpired(token);
            
            System.out.println("Token validation - Email matches: " + emailMatches + ", Not expired: " + notExpired);
            System.out.println("Token email: " + email + ", UserDetails email: " + userDetails.getUsername());
            
            return emailMatches && notExpired;
        } catch (Exception e) {
            System.out.println("Erro na validação do token: " + e.getMessage());
            return false;
        }
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTokens(UserDetails userDetails, String fullname, String role, String email){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("fullname", fullname);
        claims.put("role", role);
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTokens(UserDetails userDetails, String fullname, String role, String email, String userId, String specificId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("fullname", fullname);
        claims.put("role", role);
        claims.put("userId", userId);
        if ("PACIENTE".equals(role)) {
            claims.put("pacienteId", specificId);
        } else if ("MEDICO".equals(role)) {
            claims.put("medicoId", specificId);
        }
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
                )
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
