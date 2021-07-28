
package com.example.user.thingstodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.user.thingstodo.constants.API;
import com.example.user.thingstodo.models.Task;
import com.example.user.thingstodo.models.User;
import com.example.user.thingstodo.util.AlertReceiver;
import com.example.user.thingstodo.util.RequestHandler;
import com.example.user.thingstodo.util.SharedPrefManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;

public class AddNewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    LinearLayout addTaskLL;
    LinearLayout notLoginLL;

    EditText edtTxtId;
    EditText edtTxtTask;
    EditText edtDatePicker;
    EditText edtTimePicker;
    EditText edtTxtNotes;

    Spinner repeatSpinner;
    Spinner prioritySpinner;

    Button btnAddTask;
    Button btnLoginUser;

    RecyclerView recyclerView;

    List<Task> taskList;

    boolean isUpdating = false;

    ArrayAdapter<String> adapter;
    String list[] = {"None", "Every 1 Hour", "Daily", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String priorityList[] = {"No Priority", "! Low Priority", "!! Medium Priority", "!!! High Priority"};
    Typeface tfavv;
    User user;

    //Added
    Calendar now = Calendar.getInstance();
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    public void cancel(View view) {
        NotifyMe.cancel(getApplicationContext(),"test");
        finish();
    }

    public void location(View view) {
        Intent i = new Intent(AddNewTaskActivity.this, MapsActivity.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        getSupportActionBar().setTitle("Add Task");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        user = SharedPrefManager.getInstance(this).getUser();

        addTaskLL = findViewById(R.id.addTaskLinearLayout);
        notLoginLL = findViewById(R.id.notLoginLinearLayout);

        edtTxtId = findViewById(R.id.edtTxtId);
        edtTxtTask = findViewById(R.id.edtTxtTask);
        edtDatePicker = findViewById(R.id.datePicker);
        edtTimePicker = findViewById(R.id.timePicker);
        edtTxtNotes = findViewById(R.id.edtTxtNotes);

        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(),"Date Picker Dialog");
            }
        });

//        edtDatePicker = findViewById(R.id.datePicker);
//        edtDatePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
//                datePicker.show(getSupportFragmentManager(), "date picker");
//            }
//        });
//
//        edtTimePicker = findViewById(R.id.timePicker);
//        edtTimePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
//                timePicker.show(getSupportFragmentManager(), "time picker");
//            }
//        });

        datePickerDialog = DatePickerDialog.newInstance(
                AddNewTaskActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        timePickerDialog = TimePickerDialog.newInstance(
                AddNewTaskActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );

        customSpinnerPriority();
        customSpinnerRepeat();

        taskList = new ArrayList<>();

        btnAddTask = findViewById(R.id.btnAddTask);
        btnLoginUser = findViewById(R.id.notLoginLoginUserBtn);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertTask();

            }
        });

        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();
            }
        });
        readTasks();
    }
/*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

        String strDate = format.format(c.getTime());

        edtDatePicker.setText(strDate);

        startAlarm(c);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        edtTimePicker.setText(hourOfDay + ":" + minute);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        String text = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        edtTimePicker.setText(text);

        startAlarm(c);

    }
    */


    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int unique = (int) System.currentTimeMillis();

        SharedPreferences pref = getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
        String title = pref.getString("Notification_title", "");
        String content = pref.getString("Notification_message", "");

        Log.e("TITLE NOTIFiCATION", title);
        Log.e("CONTENT NOTIFiCATION", content);

        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Notification_title", title);
        intent.putExtra("Notification_message", content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, unique, intent, 0);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        Log.d("year", "startAlarm: " + now);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }


    public void customSpinnerRepeat() {
        repeatSpinner = findViewById(R.id.spinnerRepeat);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf");
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                v.setTextSize(14);
                return v;
            }

            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                v.setTextSize(14);
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);
        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void customSpinnerPriority() {
        prioritySpinner = findViewById(R.id.spinnerPriority);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorityList) {
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = Typeface.createFromAsset(getAssets(), "fonts/raleway_reg.ttf");
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                v.setTextSize(14);
                return v;
            }

            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                v.setTextSize(14);
                return v;
            }
        };

//        adapter = ArrayAdapter.createFromResource(this, R.array.Repeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insertTask() {
        String task_title = edtTxtTask.getText().toString().trim();
        String task_date = edtDatePicker.getText().toString().trim();
        String task_time = edtTimePicker.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();
        String notes = edtTxtNotes.getText().toString().trim();

        if (TextUtils.isEmpty(task_title)) {
            edtTxtTask.setError("Please enter task");
            edtTxtTask.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(task_date)) {
            edtDatePicker.setError("Please enter date");
            edtDatePicker.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(task_time)) {
            edtTimePicker.setError("Please enter time");
            edtTimePicker.requestFocus();
            return;
        }


        //notes is nullable
        HashMap<String, String> params = new HashMap<>();
        params.put("task_title", task_title);
        params.put("task_date", task_date);
        params.put("task_time", task_time);
        params.put("priority", priority);
        params.put("notes", notes);
        params.put("user_id", String.valueOf(user.getUser_id()));

        SharedPreferences pref = getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Notification_title", task_title);
        editor.putString("Notification_message", notes);
        editor.commit();

        startAlarm(now);

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_INSERT_TASK, params, CODE_POST_REQUEST);
        request.execute();

        edtTxtTask.setText("");
        edtDatePicker.setText("");
        edtTimePicker.setText("");
        prioritySpinner.setSelection(0);
        edtTxtNotes.setText("");
    }

    public void readTasks() {
        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_GET_TASK, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateTask() {
        String task_id = edtTxtId.getText().toString();
        String task_title = edtTxtTask.getText().toString().trim();
        String task_date = edtDatePicker.getText().toString().trim();
        String task_time = edtTimePicker.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();
        String notes = edtTxtNotes.getText().toString().trim();

        if (TextUtils.isEmpty(task_title)) {
            edtTxtTask.setError("Please enter task");
            edtTxtTask.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(task_date)) {
            edtDatePicker.setError("Please enter date");
            edtDatePicker.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(task_time)) {
            edtDatePicker.setError("Please enter time");
            edtDatePicker.requestFocus();
            return;
        }

        //notes is nullable
        HashMap<String, String> params = new HashMap<>();
        params.put("task_id", task_id);
        params.put("task_title", task_title);
        params.put("task_date", task_date);
        params.put("task_time", task_time);
        params.put("priority", priority);
        params.put("notes", notes);

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_UPDATE_TASK, params, CODE_POST_REQUEST);
        request.execute();

        btnAddTask.setText("Add");

        edtTxtTask.setText("");
        edtDatePicker.setText("");
        edtTimePicker.setText("");
        prioritySpinner.setSelection(0);
        edtTxtNotes.setText("");

        isUpdating = false;
    }

    private void deleteTask(int task_id) {
        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_DELETE_TASK + task_id, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshTaskList(JSONArray tasks) throws JSONException {
        taskList.clear();

        for (int i = 0; i < tasks.length(); i++) {
            JSONObject obj = tasks.getJSONObject(i);

            taskList.add(new Task(
                    obj.getInt("task_id"),
                    obj.getString("task_title"),
                    obj.getString("task_date"),
                    obj.getString("task_time"),
                    obj.getString("priority"),
                    obj.getString("notes")
            ));
        }

        TaskAdapter adapter = new TaskAdapter(AddNewTaskActivity.this, taskList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

        String strDate = format.format(now.getTime());

        edtDatePicker.setText(strDate);

        timePickerDialog.show(getFragmentManager(),"Time Picker Dialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, 00);

        String text = DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime());

        edtTimePicker.setText(text);


    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;

        HashMap<String, String> params;

        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshTaskList(object.getJSONArray("tasks"));
                    Intent i = new Intent(AddNewTaskActivity.this, DisplayTaskActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SharedPrefManager.getInstance(this).getUser();
        if (user.getUser_id() > 0) {
            addTaskLL.setVisibility(View.VISIBLE);
            notLoginLL.setVisibility(View.GONE);
        } else {
            addTaskLL.setVisibility(View.GONE);
            notLoginLL.setVisibility(View.VISIBLE);
        }
    }
}
