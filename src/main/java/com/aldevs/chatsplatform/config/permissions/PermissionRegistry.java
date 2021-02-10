package com.aldevs.chatsplatform.config.permissions;

import org.springframework.stereotype.Component;

@Component("p")
public class PermissionRegistry {

    protected static final String CREATE = "CREATE";
    protected static final String READ = "READ";
    protected static final String UPDATE = "UPDATE";
    protected static final String DELETE = "DELETE";



    @Component("mpr")
    public static class MessagePermissionRegistry {
        public String create() {
            return CREATE;
        }
        public String read() {
            return READ;
        }
        public String update() {
            return UPDATE;
        }
        public String delete() {
            return DELETE;
        }
    }

    @Component("cpr")
    public static class ChatPermissionRegistry {
        public String create() {
            return CREATE;
        }
        public String read() {
            return READ;
        }
        public String update() {
            return UPDATE;
        }
        public String delete() {
            return DELETE;
        }
    }

}
