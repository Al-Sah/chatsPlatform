package com.aldevs.chatsplatform.resoursses;

import lombok.Data;

@Data
public class messageErrorResponse {
    private String message;

    public messageErrorResponse(String message){
        this.message = message;
    }

}
