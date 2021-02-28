package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.ChatAction;
import lombok.Data;

import java.util.Date;

@Data
public class ActionDTO {

    private Date timestamp;
    private String action;

    public ActionDTO(ChatAction action) {
        this.timestamp = action.getTimestamp();
        this.action = action.getAction();
    }
}
