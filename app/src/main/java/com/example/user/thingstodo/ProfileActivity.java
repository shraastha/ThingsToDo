package com.example.user.thingstodo;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.thingstodo.util.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    TextView loggedUserName, loggedUserEmail;
    Button btnfeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        loggedUserName = findViewById(R.id.loggedUsername);
        loggedUserEmail = findViewById(R.id.loggedEmail);

        btnfeedback = findViewById(R.id.feedback);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            String username = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername();
            loggedUserName.setText(username);

            String email = SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail();
            loggedUserEmail.setText(email);
        }
    }

    public void logoutBtn(View view){
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void feedbackBtn(View view) {
        Intent i = new Intent(ProfileActivity.this, FeedbackActivity.class);
        startActivity(i);
    }


}
