package com.example.user.thingstodo.models;

/**
 * Created by user on 5/4/2019.
 */

public class User {

    private int user_id;
    private String username, email;

    public User(int user_id, String username, String email) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
