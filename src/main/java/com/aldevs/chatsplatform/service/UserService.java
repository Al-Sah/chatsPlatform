package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.entity.User;
import com.aldevs.chatsplatform.forms.ResponseUser;
import com.aldevs.chatsplatform.forms.SaveUser;

public interface UserService {

    void validateUser(SaveUser saveUser);
    User saveUser(SaveUser saveUser);
    ResponseUser formResponse(User user);
}
