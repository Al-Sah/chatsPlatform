package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.Role;
import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.exeption.DataValidationException;
import com.aldevs.chatsplatform.exeption.ObjectExistException;
import com.aldevs.chatsplatform.forms.RegistrationUser;
import com.aldevs.chatsplatform.repository.UserRepository;
import com.aldevs.chatsplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void validateUser(RegistrationUser saveUser){
        if(userRepository.existsByUsername(saveUser.getUsername())){
            throw new ObjectExistException("User with name [ " + saveUser.getUsername() + " ] exists!");
        }
        if(!(saveUser.getPassword().equals(saveUser.getConfirmPassword()))){
            throw new DataValidationException("Wrong confirm password");
        }
        if(saveUser.getPassword().length() < 8){
            throw new DataValidationException("Small password");
        }
         // TODO validator
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
        return user;
    }


}
