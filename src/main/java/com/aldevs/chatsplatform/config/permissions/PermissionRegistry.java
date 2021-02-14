package com.aldevs.chatsplatform.config.permissions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

public class PermissionRegistry {

    public enum Action {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        JOIN,
        LEAVE,
        SETUP,
        U_ADD,
        U_DELL
    }
    public enum Target {
        CHAT,
        MESSAGE
    }

    @Data
    @AllArgsConstructor
    public static class PermissionPair {
        private Target target;
        private Action action;
    }

    @Component("mpr")
    protected static class MessagePermissionRegistry {
        public PermissionPair create() {
            return new PermissionPair(Target.MESSAGE, Action.CREATE);
        }
        public PermissionPair read() {
            return new PermissionPair(Target.MESSAGE, Action.READ);
        }
        public PermissionPair update() {
            return new PermissionPair(Target.MESSAGE, Action.UPDATE);
        }
        public PermissionPair delete() {
            return new PermissionPair(Target.MESSAGE, Action.DELETE);
        }
    }

    @Component("cpr")
    protected static class ChatPermissionRegistry {
        public PermissionPair set() {
            return new PermissionPair(Target.CHAT, Action.SETUP);
        }
        public PermissionPair read() {
            return new PermissionPair(Target.CHAT, Action.READ);
        }
        public PermissionPair update() {
            return new PermissionPair(Target.CHAT, Action.READ);
        }
        public PermissionPair delete() {
            return new PermissionPair(Target.CHAT, Action.DELETE);
        }
        public PermissionPair join() {
            return new PermissionPair(Target.CHAT, Action.JOIN);
        }
        public PermissionPair leave() {
            return new PermissionPair(Target.CHAT, Action.LEAVE);
        }
        public PermissionPair u_add() {
            return new PermissionPair(Target.CHAT, Action.U_ADD);
        }
        public PermissionPair u_dell() {
            return new PermissionPair(Target.CHAT, Action.U_DELL);
        }
    }
}
