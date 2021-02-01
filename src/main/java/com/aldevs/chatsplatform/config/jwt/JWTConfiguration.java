package com.aldevs.chatsplatform.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JWTConfiguration {

    private Long expiredTime = 180000L;
}
