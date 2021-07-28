package com.example.user.thingstodo;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.user.thingstodo.models.User;
import com.example.user.thingstodo.util.SharedPrefManager;

public class OverviewActivity extends AppCompatActivity {

    User user;
    CoordinatorLayout displayOverviewCL;
    LinearLayout notLoginLL;
    Button btnNotLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        displayOverviewCL = findViewById(R.id.taskOverviewCoordinatorLayout);
        notLoginLL = findViewById(R.id.notLoginLinearLayout);

        btnNotLogin = findViewById(R.id.notLoginLoginUserBtn);

        btnNotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    public void addNewTask(View view) {
        Intent i = new Intent(OverviewActivity.this, AddNewTaskActivity.class);
        startActivity(i);
    }

    public void highPriority(View view) {
        Intent i = new Intent(OverviewActivity.this, HighPriorityActivity.class);
        startActivity(i);
    }

    public void mediumPriority(View view) {
        Intent i = new Intent(OverviewActivity.this, MediumPriorityActivity.class);
        startActivity(i);
    }

    public void lowPriority(View view) {
        Intent i = new Intent(OverviewActivity.this, LowPriorityActivity.class);
        startActivity(i);
    }

    public void dailyTask(View view) {
        Intent i = new Intent(OverviewActivity.this, DailyTaskActivity.class);
        startActivity(i);
    }

    public void weeklyTask(View view) {
        Intent i = new Intent(OverviewActivity.this, WeeklyActivity.class);
        startActivity(i);
    }

    public void completedTask(View view) {
        Intent i = new Intent(OverviewActivity.this, CompletedTaskActivity.class);
        startActivity(i);
    }

    public void dueTask(View view) {
        Intent i = new Intent(OverviewActivity.this, DueTaskActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SharedPrefManager.getInstance(this).getUser();
        if (user.getUser_id() > 0){
            displayOverviewCL.setVisibility(View.VISIBLE);
            notLoginLL.setVisibility(View.GONE);
        } else {
            displayOverviewCL.setVisibility(View.GONE);
            notLoginLL.setVisibility(View.VISIBLE);
        }
    }

}
