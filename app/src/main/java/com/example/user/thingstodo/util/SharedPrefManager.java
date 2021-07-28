package com.example.user.thingstodo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.thingstodo.models.User;

/**
 * Created by user on 5/3/2019.
 */

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME = "thingstodo_shared_pref";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";


    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUser(User user){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getUser_id());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_USER_EMAIL, user.getEmail());

        editor.apply();

    }

    public User getUser(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User user = new User(
                sharedPreferences.getInt(KEY_USER_ID, -1),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null)
        );

        return user;
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //return sharedPreferences.getInt(KEY_USER_ID, -1) != -1;
        if (sharedPreferences.getInt(KEY_USER_ID, -1) != -1){
            return true;
        }
        return false;
    }

    public void logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
