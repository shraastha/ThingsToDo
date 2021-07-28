package com.example.user.thingstodo.constants;

import com.example.user.thingstodo.SubTaskResponse;
import com.example.user.thingstodo.util.LoginResponse;
import com.example.user.thingstodo.fragments.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user on 5/3/2019.
 */

public interface UserAPI {


    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser (
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateuser")
    Call<LoginResponse> updateUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("createsubtask")
    Call<DefaultResponse> createSubTask(
            @Field("subtask") String subtask,
            @Field("task_id") int task_id
    );

    @FormUrlEncoded
    @POST("userallsubtask")
    Call<SubTaskResponse> userAllSubTask(
            @Field("task_id") int task_id
    );

    @FormUrlEncoded
    @POST("updatesubtask")
    Call<SubTaskResponse> updateSubTask(
            @Field("subtask") String subtask,
            @Field("subtask_id") int subtask_id
    );

    @FormUrlEncoded
    @POST("deletesubtask")
    Call<DefaultResponse> deleteSubTask(
            @Field("subtask_id") int subtask_id
    );

}