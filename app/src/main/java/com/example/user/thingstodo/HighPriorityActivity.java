package com.example.user.thingstodo;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighPriorityActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;

    List<Task> taskList;
    List<SubTask> subTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_priority);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        taskList = new ArrayList<>();
        subTaskList = new ArrayList<>();

        recyclerView = findViewById(R.id.taskRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readTasks();
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
        User user = SharedPrefManager.getInstance(this).getUser();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user.getUser_id()));

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_HP_TASK, params, CODE_POST_REQUEST);
        request.execute();
    }


    private void refreshTaskList(JSONArray tasks) throws JSONException {
//        taskList.clear();

        for (int i = 0; i < tasks.length(); i++) {
            JSONObject obj = tasks.getJSONObject(i);

            int taskId = obj.getInt("task_id");

            Call<SubTaskResponse> call = RetrofitClient.getInstance().getApi().userAllSubTask(taskId);
            call.enqueue(new Callback<SubTaskResponse>() {
                @Override
                public void onResponse(Call<SubTaskResponse> call, Response<SubTaskResponse> response) {

                    SubTaskResponse subTaskResponse = response.body();
                    if (subTaskResponse.isSuccess()){
                        Log.e("LOGIN", String.valueOf(subTaskResponse.getSubtasks_list()));
                        for (int i=0; i<subTaskResponse.getSubtasks_list().size(); i++) {
                            SubTask subTask = subTaskResponse.getSubtasks_list().get(i);
                            Log.e("SUBTASK ID", String.valueOf(subTask.getSubtask_id()));
                            Log.e("SUBTASK TITLE", String.valueOf(subTask.getSubtask()));
                            Log.e("TASK ID", String.valueOf(subTask.getTask_id()));
                            SubTask subItem = new SubTask(subTask.getSubtask_id(), subTask.getSubtask(),subTask.getTask_id());
                            subTaskList.add(subItem);
                        }
                    }

                }

                @Override
                public void onFailure(Call<SubTaskResponse> call, Throwable t) {

                }
            });

            taskList.add(new Task(
                    obj.getInt("task_id"),
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
}
