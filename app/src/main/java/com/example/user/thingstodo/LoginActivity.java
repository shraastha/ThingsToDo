package com.example.user.thingstodo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.thingstodo.util.LoginResponse;
import com.example.user.thingstodo.util.RetrofitClient;
import com.example.user.thingstodo.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    TextInputLayout input_layout_password, input_layout_username;
    public String username;
    String password;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);

        input_layout_password = findViewById(R.id.input_layout_password);
        input_layout_username = findViewById(R.id.input_layout_username);

        edtUsername = findViewById(R.id.edtTxtUsername);
        edtPassword = findViewById(R.id.edtTxtPassword);

        input_layout_password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf"));

        edtPassword = findViewById(R.id.edtTxtPassword);
        edtPassword.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf"));
    }

    private boolean checkConnectivity() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            Toast.makeText(getApplicationContext(), "Couldn't find any internet connection!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateUsername(){
        String usernameInput = input_layout_username.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()){
            input_layout_username.setError("Field can't be empty. Please enter username.");
            return false;
        } else {
            input_layout_username.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = input_layout_password.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()){
            input_layout_password.setError("Field can't be empty. Please enter password.");
            return false;
        } else {
            input_layout_password.setError(null);
            return true;
        }
    }


    public void logUser(View view) {

        if (!validateUsername() | !validatePassword()){
            return;
        }

        if (checkConnectivity()) {
            username = edtUsername.getText().toString().trim();
            password = edtPassword.getText().toString().trim();

            dialog.setTitle("Please Wait...");
            dialog.setMessage("Preparing data...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Call<LoginResponse> call = RetrofitClient.getInstance().getApi().userLogin(username, password);
Log.e("TEST !!! ", "1");
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()){

                        Log.e("LOGIN", loginResponse.getMessage());
                        Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(getApplicationContext()).saveUser(loginResponse.getUser());
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Login Fail")
                                .setMessage(loginResponse.getMessage())
                                .setIcon(R.drawable.ic_info_outline_black_24dp)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });

            //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            //backgroundWorker.execute(type, username, password);
        }

//        SharedPreferences pref = getSharedPreferences("UserDetails", MODE_PRIVATE);
//
//        String userDetails = pref.getString(username + password,"Incorrect username or password");
//        String user = pref.getString(username,"Incorrect username");
//        String pass = pref.getString(password,"Incorrect password");
//
//        if (user.equals(username) && pass.equals(password)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("display", userDetails);
//            editor.commit();
//
//            Intent main = new Intent(view.getContext(), MainActivity.class);
//            startActivity(main);
//            edtUsername.setText("");
//            edtPassword.setText("");
//        }
//        else {
//            Toast.makeText(view.getContext(), userDetails,Toast.LENGTH_LONG).show();
//        }
    }

    public void register(View view) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
