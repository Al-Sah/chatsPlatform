package com.aldevs.chatsplatform.security;

import com.aldevs.chatsplatform.config.JWTConfiguration;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.security.Key;

@Component
@Slf4j
public class JwtAuthenticationProvider {

    private final UserDetailsImplement userDetailsService;
    private Key secretKey;
    private final Long validityTimeMls;

    public JwtAuthenticationProvider(UserDetailsImplement userDetailsService, JWTConfiguration jwtConfiguration) {
        this.userDetailsService = userDetailsService;
        this.validityTimeMls = jwtConfiguration.getValidTime();
    }

    @PostConstruct
    private void initSecretKey() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username){
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTimeMls);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Expired or invalid token");
        }
        return !claims.getBody().getExpiration().before(new Date());
    }
}
