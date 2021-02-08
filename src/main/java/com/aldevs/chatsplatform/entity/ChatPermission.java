package com.aldevs.chatsplatform.entity;

/**
 * Main types of chat Permission are: CREATOR, ADMIN, BASIC. CREATOR has full excess,
 * ADMIN cannot add or delete other ADMIN and BASIC is simple CRUD
 * <p>Nevertheless, creator or admin will have possibility to set custom permission set.<p>
 * e.g: CHAT_SIMPLE_PARTICIPANT covers basic CRUD operation, but you have possibility to add [EDIT_NOT_MINE_MESSAGES or DELETE_NOT_MINE_MESSAGES] permissions
 * @see java.lang.Enum
 */
public enum ChatPermission {

    GROUP_CHAT_CREATOR,
    GROUP_CHAT_ADMIN,
    CHAT_BASIC,

    READ_MESSAGES,
    SEND_MESSAGES,
    EDIT_MESSAGES,
    DELETE_MESSAGES,

    EDIT_NOT_MINE_MESSAGES,
    DELETE_NOT_MINE_MESSAGES,
    ADD_NEW_USER,
    DELETE_USER,

    SET_USER_AS_ADMIN,
    SET_USER_AS_SIMPLE_PARTICIPANT

}
