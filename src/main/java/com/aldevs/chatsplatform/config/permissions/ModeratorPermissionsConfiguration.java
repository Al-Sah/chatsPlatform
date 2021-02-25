package com.aldevs.chatsplatform.config.permissions;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "moderator.permissions")
public final class ModeratorPermissionsConfiguration {


    public Boolean sendMessagesToLocalChat = false;
    public Boolean sendMessagesToPublicGroup = false;
    public Boolean sendMessagesToPrivateGroup = false;

    public Boolean readMessagesInLocalChat = false;
    public Boolean readMessagesInPublicGroup = false;
    public Boolean readMessagesInPrivateGroup = false;

    public Boolean editMessagesInLocalChat = false;
    public Boolean editMessagesInPublicGroup = false;
    public Boolean editMessagesInPrivateGroup = false;

    public Boolean deleteMessagesInLocalChat = false;
    public Boolean deleteMessagesInPublicGroup = false;
    public Boolean deleteMessagesInPrivateGroup = false;

    public Boolean deleteLocalChat = false;
    public Boolean deletePublicGroup = false;
    public Boolean deletePrivateGroup = false;
}