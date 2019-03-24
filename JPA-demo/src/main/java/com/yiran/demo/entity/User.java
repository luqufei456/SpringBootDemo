package com.yiran.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "t_user")
public class User implements Serializable {
    private static final long serialVersionUID = -3812675144486946279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // TODO 忽略该字段的映射
    @Transient
    private String email;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
