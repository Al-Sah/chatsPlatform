package com.aldevs.chatsplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ElementCollection
    @CollectionTable(name = "chats_participants", joinColumns = @JoinColumn(name = "chat_id"))
    private List<String> participants;

    @Column(name = "chat_uuid")
    private String chatUUID;
    private String creator;

    public Chat(List<String> participants, String chatUUID, String creator) {
        this.participants = participants;
        this.chatUUID = chatUUID;
        this.creator = creator;
    }
}
