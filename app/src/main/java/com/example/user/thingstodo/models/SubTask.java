package com.example.user.thingstodo.models;

/**
 * Created by user on 5/26/2019.
 */

public class SubTask {

    int subtask_id;
    String subtask;
    int task_id;

    public SubTask(int subtask_id, String subtask, int task_id) {
        this.subtask_id = subtask_id;
        this.subtask = subtask;
        this.task_id = task_id;
    }

    public int getSubtask_id() {
        return subtask_id;
    }

    public void setSubtask_id(int subtask_id) {
        this.subtask_id = subtask_id;
    }

    public String getSubtask() {
        return subtask;
    }

    public void setSubtask(String subtask) {
        this.subtask = subtask;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
}
