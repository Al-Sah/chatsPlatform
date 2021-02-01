package com.aldevs.chatsplatform;

import com.aldevs.chatsplatform.config.jwt.JWTConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties({
        JWTConfiguration.class
})
public class ChatsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatsPlatformApplication.class, args);
    }

}
