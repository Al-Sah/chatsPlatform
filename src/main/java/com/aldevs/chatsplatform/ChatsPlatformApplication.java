package com.aldevs.chatsplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ChatsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatsPlatformApplication.class, args);
    }

}
