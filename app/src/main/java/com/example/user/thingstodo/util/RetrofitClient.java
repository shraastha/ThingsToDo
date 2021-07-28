package com.example.user.thingstodo.util;

import android.content.Context;

import com.example.user.thingstodo.R;
import com.example.user.thingstodo.constants.UserAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 5/3/2019.
 */

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.8:81/thingstodo/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
//    private static Context context;
//    private static final String route = context.getString(R.string.route);

    private RetrofitClient () {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public UserAPI getApi(){
        return retrofit.create(UserAPI.class);
    }


}
