package com.yiran.demo.entity;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -3812675144486946279L;

    private Long id;
    private String username;
    private String passowrd;

    public User(){}

    public User(String username, String passowrd) {
        this.username = username;
        this.passowrd = passowrd;
    }

    public User(Long id, String username, String passowrd) {
        this.id = id;
        this.username = username;
        this.passowrd = passowrd;
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

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passowrd='" + passowrd + '\'' +
                '}';
    }
}
