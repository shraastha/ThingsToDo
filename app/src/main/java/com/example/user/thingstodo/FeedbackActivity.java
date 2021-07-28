package com.example.user.thingstodo;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {

    TextInputLayout subjectTIL, msgTIL;
    EditText editTextSubject, editTextMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        subjectTIL =  findViewById(R.id.subjectFeedbackTIL);
        msgTIL =  findViewById(R.id.msgFeedbackTIL);

        editTextSubject =  findViewById(R.id.subjectFeedback);
        editTextMsg =  findViewById(R.id.msgFeedback);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


    }

    private boolean checkSubject(){
        String fullnameInput = subjectTIL.getEditText().getText().toString().trim();
        if (fullnameInput.isEmpty()){
            subjectTIL.setError("This field is required");
            return false;
        } else {
            subjectTIL.setError(null);
            return true;
        }
    }

    private boolean checkMsg(){
        String msgInput = msgTIL.getEditText().getText().toString().trim();
        if (msgInput.isEmpty()){
            msgTIL.setError("This field is required");
            return false;
        } else {
            msgTIL.setError(null);
            return true;
        }
    }

    public void sendFeedback(View view){
        if (!checkSubject() | !checkMsg()){
            return;
        }

        String subject = editTextSubject.getText().toString();
        String msg = editTextMsg.getText().toString();

        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "shr.aastha@gmail.com"});
        Email.putExtra(Intent.EXTRA_SUBJECT, subject);
        Email.putExtra(Intent.EXTRA_TEXT, msg);

        Email.setType("message/rfc822");
        startActivity(Intent.createChooser(Email, "Choose app to send email"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
