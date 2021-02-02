package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.forms.auth.*;

public interface UserService {

    User saveUser(RegistrationUser saveUser);
    boolean existByUsername(String username);
    User findByUsername(String username);
    AuthenticatedUser loginUser(AuthenticationUser authenticationUser);
}
