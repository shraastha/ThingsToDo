package com.example.user.thingstodo.util;

import com.example.user.thingstodo.models.User;

/**
 * Created by user on 5/4/2019.
 */

public class LoginResponse {

    private boolean success;
    private String message;
    private User user;

    public LoginResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
