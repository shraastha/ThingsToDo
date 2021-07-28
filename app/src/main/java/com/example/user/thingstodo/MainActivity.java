package com.example.user.thingstodo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.user.thingstodo.fragments.DashboardFragment;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener{

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    public void addNewTask(View view) {
            Intent i = new Intent(MainActivity.this, AddNewTaskActivity.class);
            startActivity(i);
    }

    public void overview(View view){
        Intent i = new Intent(MainActivity.this, DisplayTaskActivity.class);
        startActivity(i);
    }

    public void login(View view){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void display(View view) {
        Intent i = new Intent(MainActivity.this, OverviewActivity.class);
        startActivity(i);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();

        //Dashboard instantiate
        DashboardFragment dashboardFragment = new DashboardFragment();

        fragmentTransaction.replace(R.id.containerMain, dashboardFragment);
        fragmentTransaction.commit();
//        DashboardFragment dashboardFragment = new DashboardFragment();
//
//        fragmentTransaction.replace(R.id.containerMain, dashboardFragment);
//        fragmentTransaction.commit();


        getSupportActionBar().hide();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        back_pressed = System.currentTimeMillis();
    }
}
