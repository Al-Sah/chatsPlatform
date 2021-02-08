package com.aldevs.chatsplatform.config.permissions;

import org.springframework.stereotype.Component;

@Component("p")
public class PermissionRegistry {

    public String create() {
        return "CREATE";
    }
    public String read() {
        return "READ";
    }
    public String update() {
        return "UPDATE";
    }
    public String delete() {
        return "DELETE";
    }
}
