package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.*;
import com.aldevs.chatsplatform.exeption.UserExistException;
import com.aldevs.chatsplatform.forms.auth.AuthenticatedUser;
import com.aldevs.chatsplatform.forms.auth.AuthenticationUser;
import com.aldevs.chatsplatform.forms.auth.RegistrationUser;
import com.aldevs.chatsplatform.repositories.UserRepository;
import com.aldevs.chatsplatform.security.JwtProvider;
import com.aldevs.chatsplatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }
    private void authenticate(AuthenticationUser authenticationUser){
        User user = findByUsername(authenticationUser.getUsername());
        if(!passwordEncoder.matches(authenticationUser.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public User saveUser(RegistrationUser saveUser){
        if(userRepository.existsByUsername(saveUser.getUsername())){
            throw new UserExistException(saveUser.getUsername());
        }
        var user = new User(
                saveUser.getProfileName(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                saveUser.getUserAbout(),
                passwordEncoder.encode(saveUser.getPassword()),
                Collections.singleton(Role.ROLE_USER)
        );
        userRepository.save(user);
        log.info("User [ " + user.getUsername() + " ] registered successfully");
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("User with username: " + username + " not found"));
    }
    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public AuthenticatedUser loginUser(AuthenticationUser authenticationUser) {
        try {
            authenticate(authenticationUser);
            String token = jwtProvider.generateToken(authenticationUser.getUsername());
            return new AuthenticatedUser(authenticationUser.getUsername(), token);
        } catch (AuthenticationException authenticationException) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
