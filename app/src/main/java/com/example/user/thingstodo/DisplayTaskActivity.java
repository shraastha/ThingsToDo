package com.example.user.thingstodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.thingstodo.constants.API;
import com.example.user.thingstodo.models.SubTask;
import com.example.user.thingstodo.models.Task;
import com.example.user.thingstodo.models.User;
import com.example.user.thingstodo.util.RequestHandler;
import com.example.user.thingstodo.util.RetrofitClient;
import com.example.user.thingstodo.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    CoordinatorLayout displayTaskCL;
    LinearLayout notLoginLL;

    EditText updateTxtId;
    EditText updateTxtTask;
    EditText updateDatePicker;
    EditText updateTimePicker;
    EditText updateTxtNotes;

    Spinner updateRepeatSpinner;
    Spinner updatePrioritySpinner;

    Button btnNotLogin;

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;

    List<Task> taskList;
    List<SubTask> subTaskList;
    User user;

    ArrayAdapter<String> adapter;
    String list[] = {"None", "Daily", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String priorityList[] = {"No Priority", "! Low Priority", "!! Medium Priority", "!!! High Priority"};
    Typeface tfavv;

    public void cancelDialog(View view) {
        finish();
    }

    public void updatelocation(View view) {
        Intent i = new Intent(DisplayTaskActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        displayTaskCL = findViewById(R.id.displayTaskCoordinatorLayout);
        notLoginLL = findViewById(R.id.notLoginLinearLayout);

        updateTxtId = findViewById(R.id.updateTxtId);
        updateTxtTask = findViewById(R.id.updateTxtTask);
        updateTxtNotes = findViewById(R.id.updateTxtNotes);

        btnNotLogin = findViewById(R.id.notLoginLoginUserBtn);

        btnNotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        taskList = new ArrayList<>();
        subTaskList = new ArrayList<>();
        user = SharedPrefManager.getInstance(this).getUser();

        recyclerView = findViewById(R.id.taskRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readTasks();
    }

    public void addNewTask(View view) {
        Intent i = new Intent(DisplayTaskActivity.this, AddNewTaskActivity.class);
        startActivity(i);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        String strDate = format.format(c.getTime());

        updateDatePicker.setText(strDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        updateTimePicker.setText(hourOfDay + ":" + minute);
    }

    public void updateSpinnerRepeat() {
        updateRepeatSpinner = findViewById(R.id.spinnerRepeat);
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

//        adapter = ArrayAdapter.createFromResource(this, R.array.Repeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateRepeatSpinner.setAdapter(adapter);
        updateRepeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateSpinnerPriority() {
        updatePrioritySpinner = findViewById(R.id.spinnerPriority);
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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updatePrioritySpinner.setAdapter(adapter);
        updatePrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
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

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshTaskList(object.getJSONArray("tasks"));
                    Log.e("ghh", object.getString("message") );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
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

    public void readTasks() {

        HashMap<String, String> params = new HashMap<>();
        Log.e("USER ID", String.valueOf(user.getUser_id()));
        params.put("user_id", String.valueOf(user.getUser_id()));

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_GET_TASK, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void updateTask() {
        String task_id = updateTxtId.getText().toString();
        String task_title = updateTxtTask.getText().toString().trim();
        String task_date = updateDatePicker.getText().toString().trim();
        String task_time = updateTimePicker.getText().toString().trim();
        String priority = updatePrioritySpinner.getSelectedItem().toString();
        String notes = updateTxtNotes.getText().toString().trim();

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

        updateTxtTask.setText("");
        updateDatePicker.setText("");
        updateTimePicker.setText("");
        updatePrioritySpinner.setSelection(0);
        updateTxtNotes.setText("");
    }

    private void getSubTaskData(){

    }

    private void refreshTaskList(JSONArray tasks) throws JSONException {
//        taskList.clear();

        for (int i = 0; i < tasks.length(); i++) {
            JSONObject obj = tasks.getJSONObject(i);

            int taskId = obj.getInt("task_id");

            Log.e("TASK ID", String.valueOf(taskId));
            subTaskList.clear();
            if (subTaskList.size()>0){
            Call<SubTaskResponse> call = RetrofitClient.getInstance().getApi().userAllSubTask(taskId);
            call.enqueue(new Callback<SubTaskResponse>() {
                @Override
                public void onResponse(Call<SubTaskResponse> call, Response<SubTaskResponse> response) {
                    subTaskList.clear();
                    SubTaskResponse subTaskResponse = response.body();
//                    Log.e("LOGIN", subTaskResponse.getMessage());
//                    Log.e("LOGIN", String.valueOf(subTaskResponse.isSuccess()));
                    if (subTaskResponse.isSuccess()){
                        Log.e("LOGIN", String.valueOf(subTaskResponse.getSubtasks_list()));

                        Log.e("SIZE OF SUBTASK LIST", String.valueOf(subTaskList.size()));
                        subTaskList.clear();
                        for (int i=0; i<subTaskResponse.getSubtasks_list().size(); i++) {
                            SubTask subTask = subTaskResponse.getSubtasks_list().get(i);
                            Log.e("SUBTASK ID", String.valueOf(subTask.getSubtask_id()));
                            Log.e("SUBTASK TITLE", String.valueOf(subTask.getSubtask()));
                            Log.e("TASK ID", String.valueOf(subTask.getTask_id()));
                            SubTask subItem = new SubTask(subTask.getSubtask_id(), subTask.getSubtask(),subTask.getTask_id());
                            subTaskList.add(subItem);
                        }
                        Log.e("SUBTASK LIST", String.valueOf(subTaskList));
                    }

                }

                @Override
                public void onFailure(Call<SubTaskResponse> call, Throwable t) {

                }
            });
            } else {

            }

            taskList.add(new Task(
                    taskId,
                    obj.getString("task_title"),
                    obj.getString("task_date"),
                    obj.getString("task_time"),
                    obj.getString("priority"),
                    obj.getString("notes"),
                    subTaskList
            ));

        }

        taskAdapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                taskAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SharedPrefManager.getInstance(this).getUser();
        if (user.getUser_id() > 0){
            displayTaskCL.setVisibility(View.VISIBLE);
            notLoginLL.setVisibility(View.GONE);
        } else {
            displayTaskCL.setVisibility(View.GONE);
            notLoginLL.setVisibility(View.VISIBLE);
        }
    }
}
