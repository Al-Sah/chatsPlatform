package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.forms.AuthenticatedUser;
import com.aldevs.chatsplatform.forms.AuthenticationUser;
import com.aldevs.chatsplatform.forms.RegistrationUser;

public interface UserService {

    void validateUser(RegistrationUser saveUser);
    User saveUser(RegistrationUser saveUser);
    User findByUsername(String username);
    AuthenticatedUser loginUser(AuthenticationUser authenticationUser);
}
