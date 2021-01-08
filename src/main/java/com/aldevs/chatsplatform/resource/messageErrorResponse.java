package com.aldevs.chatsplatform.resource;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class messageErrorResponse {
    @NotBlank
    private String message;

    public messageErrorResponse(String message){
        this.message = message;
    }

}
