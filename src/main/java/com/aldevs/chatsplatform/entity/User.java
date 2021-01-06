package com.aldevs.chatsplatform.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileName;
    //@Column(name = "username", unique = true)
    private String username;
    private String email;
    private String userAbout;
    private String password;


}
