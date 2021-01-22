package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.Role;
import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.exeption.DataValidationException;
import com.aldevs.chatsplatform.exeption.ObjectExistException;
import com.aldevs.chatsplatform.forms.AuthenticatedUser;
import com.aldevs.chatsplatform.forms.AuthenticationUser;
import com.aldevs.chatsplatform.forms.RegistrationUser;
import com.aldevs.chatsplatform.repository.UserRepository;
import com.aldevs.chatsplatform.security.JwtAuthenticationProvider;
import com.aldevs.chatsplatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider authenticationProvider;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtAuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
    }
    private void authenticate(AuthenticationUser authenticationUser){
        String username = authenticationUser.getUsername();
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        if(!passwordEncoder.matches(authenticationUser.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public void validateUser(RegistrationUser saveUser){
        if(userRepository.existsByUsername(saveUser.getUsername())){
            throw new ObjectExistException("User with name [ " + saveUser.getUsername() + " ] exists!");
        }
        if(saveUser.getPassword().length() < 8){
            throw new DataValidationException("Small password");
        }// TODO validator
    }

    @Override
    public User saveUser(RegistrationUser saveUser){
        validateUser(saveUser);
        var user = new User(
                saveUser.getProfileName(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                saveUser.getUserAbout(),
                passwordEncoder.encode(saveUser.getPassword()),
                Collections.singleton(Role.USER)
        );
        userRepository.save(user);
        log.info("[UserService] User [ " + user.getUsername() + " ] registered successfully");
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AuthenticatedUser loginUser(AuthenticationUser authenticationUser) {
        try {
            authenticate(authenticationUser);
            String token = authenticationProvider.generateToken(authenticationUser.getUsername());
            log.info("[UserService] Generated token for user: " + authenticationUser.getUsername());
            return new AuthenticatedUser(authenticationUser.getUsername(), token);
        } catch (AuthenticationException authenticationException) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
