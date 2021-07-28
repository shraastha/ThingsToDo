package com.example.user.thingstodo.models;


import java.util.List;

/**
 * Created by user on 4/3/2019.
 */

public class Task {
    private int task_id;
    private String task_title, task_date, task_time, priority, notes;
    private List<SubTask> subTaskList;

    public Task (int task_id, String task_title, String task_date, String task_time, String priority, String notes){
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_date = task_date;
        this.task_time = task_time;
        this.priority = priority;
        this.notes = notes;
    }


    public Task(int task_id, String task_title, String task_date, String task_time, String priority, String notes, List<SubTask> subTaskList) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_date = task_date;
        this.task_time = task_time;
        this.priority = priority;
        this.notes = notes;
        this.subTaskList = subTaskList;
    }

    public int getTask_id() {
        return task_id;
    }

    public String getTask_title() { return task_title; }

    public String getTask_date() {
        return task_date;
    }

    public String getTask_time() {
        return task_time;
    }

    public String getPriority() {
        return priority;
    }

    public String getNotes() {
        return notes;
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }
}
