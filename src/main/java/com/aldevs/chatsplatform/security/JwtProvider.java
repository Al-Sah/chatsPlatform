package com.aldevs.chatsplatform.security;

import com.aldevs.chatsplatform.config.jwt.JWTConfiguration;
import com.aldevs.chatsplatform.exeption.TokenValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.security.Key;

@Component
@Slf4j
public class JwtProvider {

    private Key secretKey;
    private final Long validityTimeMls;
    private final JwtParser jwtParser;

    public JwtProvider(JWTConfiguration jwtConfiguration, JwtParser jwtParser) {
        this.validityTimeMls = jwtConfiguration.getExpiredTime();
        this.jwtParser = jwtParser;
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

    public String extractUsername(String token) {
        return jwtParser.setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public void validateToken(String token) throws TokenValidationException {
        try {
            jwtParser.setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenValidationException("Expired or invalid token");
        }
    }
}
