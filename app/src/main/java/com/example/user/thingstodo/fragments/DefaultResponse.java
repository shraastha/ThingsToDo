package com.example.user.thingstodo.fragments;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/4/2019.
 */

public class DefaultResponse {

    @SerializedName("success")
    private boolean succ;

    @SerializedName("message")
    private String msg;

    public DefaultResponse(boolean succ, String msg) {
        this.succ = succ;
        this.msg = msg;
    }

    public boolean isSucc() {
        return succ;
    }

    public String getMsg() {
        return msg;
    }
}
