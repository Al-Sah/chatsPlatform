package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.Role;
import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.exeption.DataValidationException;
import com.aldevs.chatsplatform.exeption.ObjectExistException;
import com.aldevs.chatsplatform.forms.ResponseUser;
import com.aldevs.chatsplatform.forms.SaveUser;
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
    public void validateUser(SaveUser saveUser){
        if(userRepository.existsByUsername(saveUser.getUsername())){
            throw new ObjectExistException("User with name [ " + saveUser.getUsername() + " ] exists!");
        }
        if(!(saveUser.getPassword().equals(saveUser.getConfirmPassword()))){
            throw new DataValidationException("Wrong confirm password");
        }
        if(saveUser.getPassword().length() < 8){
            throw new DataValidationException("Small password");
        }
    }

    @Override
    public User saveUser(SaveUser saveUser){
        validateUser(saveUser);
        var user = new User();
        user.setUserAbout(saveUser.getUserAbout());
        user.setUsername(saveUser.getUsername());
        user.setEmail(saveUser.getEmail());
        user.setProfileName(saveUser.getProfileName());
        user.setPassword(passwordEncoder.encode(saveUser.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return user;
    }

    @Override
    public ResponseUser formResponse(User user){
        return new ResponseUser(user);
    }

}
