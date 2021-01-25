package com.aldevs.chatsplatform.security;

import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.repository.UserRepository;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final JwtAuthenticationProvider jwtProvider;
    private final UserRepository userRepository;

    public AuthenticationService(JwtAuthenticationProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        String token = authenticationToken.getName().substring(7);
        jwtProvider.validateToken(token);
        try {
            User user = userRepository.findByUsername(jwtProvider.extractUsername(token));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
