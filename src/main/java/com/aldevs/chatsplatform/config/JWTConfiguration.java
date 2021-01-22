package com.aldevs.chatsplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JWTConfiguration {

    private Long validTime;
}
