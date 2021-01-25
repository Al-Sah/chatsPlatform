package com.aldevs.chatsplatform.exeption;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TokenValidationException extends UsernameNotFoundException {
    public TokenValidationException(String msg) {
        super(msg);
    }

    public TokenValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
