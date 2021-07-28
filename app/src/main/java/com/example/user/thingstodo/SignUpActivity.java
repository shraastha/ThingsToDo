package com.example.user.thingstodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.thingstodo.fragments.DefaultResponse;
import com.example.user.thingstodo.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText edtTxtUsername, edtTxtEmail, edtTxtPassword;
    TextInputLayout txtLayoutUsername, txtLayoutEmail, txtLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        edtTxtUsername =  findViewById(R.id.txtViewUsername);
        edtTxtEmail =  findViewById(R.id.txtViewEmail);
        edtTxtPassword = findViewById(R.id.txtViewPassword);

        txtLayoutUsername = findViewById(R.id.inputLayoutUsername);
        txtLayoutEmail = findViewById(R.id.inputLayoutEmail);
        txtLayoutPassword = findViewById(R.id.inputLayoutPassword);

        txtLayoutPassword.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf"));
        edtTxtPassword.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf"));
    }

    private boolean usernameEmpty(){
        String txtUsername = txtLayoutUsername.getEditText().getText().toString().trim();

        if(!txtUsername.isEmpty()) {
            txtLayoutUsername.setError(null);
            return true;
        } else {
            txtLayoutUsername.setError("Username is required!");
            return false;
        }


    }

    private boolean emailEmpty(){
        String txtEmail = txtLayoutEmail.getEditText().getText().toString().trim();

        if(!txtEmail.isEmpty()) {
            txtLayoutEmail.setError(null);
            return true;
        } else {
            txtLayoutEmail.setError("Email is required!");
            return false;
        }
    }

    private boolean validEmail(){
        String txtEmail = txtLayoutEmail.getEditText().getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            txtLayoutEmail.setError("Enter a valid email!");
            return false;
        } else {
            txtLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean passwordEmpty(){
        String txtPassword = txtLayoutPassword.getEditText().getText().toString().trim();

        if(!txtPassword.isEmpty()) {
            txtLayoutPassword.setError(null);
            return true;
        } else {
            txtLayoutPassword.setError("Password is required!");
            return false;
        }
    }

    private boolean validPassword() {
        String txtPassword = txtLayoutPassword.getEditText().getText().toString().trim();

        if (txtPassword.length() < 6) {
            txtLayoutPassword.setError("Password should be at least 6 characters long!");
            return false;
        } else {
            return true;
        }
    }

    public void registerUser(View view) {

        if (!usernameEmpty() | !emailEmpty() | !passwordEmpty() | !validEmail() | !validPassword()) {
            return;
        }

        String username = edtTxtUsername.getText().toString();
        String email = edtTxtEmail.getText().toString();
        String password = edtTxtPassword.getText().toString();

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(username, email, password);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse defaultResponse = response.body();

                if (response.body().isSucc()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Sign Up Successful")
                            .setMessage(defaultResponse.getMsg())
                            .setIcon(R.drawable.ic_done_all_black_24dp)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Sign Up Fail")
                            .setMessage(defaultResponse.getMsg())
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
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });


//        String type = "signup";
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.execute(type, username, email, password);
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putString(username + password, "Username: " +username + "\n Email: " + email);
//        editor.putString(username, username);
//        editor.putString(password, password);
//        editor.commit();
//
//        finish();
    }

    public void SignUpLogin(View view){
        finish();
//        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
//        startActivity(i);
    }

}
