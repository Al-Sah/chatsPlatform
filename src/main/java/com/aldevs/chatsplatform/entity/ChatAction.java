package com.aldevs.chatsplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ChatAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date timestamp;
    private String action;

    public ChatAction(String action) {
        this.timestamp = new Date();
        this.action = action;
    }
}
