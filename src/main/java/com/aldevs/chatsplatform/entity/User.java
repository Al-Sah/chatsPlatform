package com.aldevs.chatsplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_name")
    private String profileName;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "user_about")
    private String userAbout;
    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public User(){}

    public User(String profileName,String username,String email,String userAbout,String password, Set<Role> roles){
        this.profileName = profileName;
        this.username = username;
        this.email = email;
        this.userAbout = userAbout;
        this.password = password;
        this.roles = roles;
    }

}
