package com.aldevs.chatsplatform.config.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtParserConfiguration {
    @Bean
    public JwtParser jwtParser() {
        return Jwts.parser();
    }

}
