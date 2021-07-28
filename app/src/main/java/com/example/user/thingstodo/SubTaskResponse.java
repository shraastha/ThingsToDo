package com.example.user.thingstodo;

import com.example.user.thingstodo.models.SubTask;
import com.example.user.thingstodo.models.User;

import java.util.List;

/**
 * Created by user on 5/26/2019.
 */

public class SubTaskResponse {

    private boolean success;
    private String message;
    List<SubTask> subtasks_list;

    public SubTaskResponse(boolean success, String message, List<SubTask> subtasks_list) {
        this.success = success;
        this.message = message;
        this.subtasks_list = subtasks_list;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<SubTask> getSubtasks_list() {
        return subtasks_list;
    }
}
