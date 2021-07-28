package com.example.user.thingstodo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.thingstodo.constants.API;
import com.example.user.thingstodo.fragments.DefaultResponse;
import com.example.user.thingstodo.models.Task;
import com.example.user.thingstodo.models.User;
import com.example.user.thingstodo.util.AlertReceiver;
import com.example.user.thingstodo.util.RequestHandler;
import com.example.user.thingstodo.util.RetrofitClient;
import com.example.user.thingstodo.util.SharedPrefManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 4/27/2019.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private List<Task> taskList;
    private List<Task> taskListAll;

    private Dialog taskDialog, addSubTaskDialog, shareTaskDialog, updateTaskDialog, detailsDialog, deleteTaskDialog;
    ProgressDialog dialog;

    private EditText updateId, updateName, updateDate, updateTime, updateNotes, shareEmail;
    private TextView shareName, shareDate, shareTime;
    private Spinner updatePriority;

    private ArrayAdapter<String> priorityAdapter;
    //    String list[] = {"None","Daily","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String priorityList[] = {"No Priority", "! Low Priority", "!! Medium Priority", "!!! High Priority"};
    private Typeface tfavv;

    Calendar now = Calendar.getInstance();
    TimePickerDialog timePickerDialog;

    User user;


    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        taskListAll = new ArrayList<>(taskList);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = SharedPrefManager.getInstance(context).getUser();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_layout, parent, false);
        final TaskViewHolder viewHolder = new TaskViewHolder(view);

        taskDialog = new Dialog(context);
//        taskDialog.setContentView(R.layout.layoutActivityNameRakhniYeta);
        taskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        taskDialog.setCancelable(false);

        viewHolder.taskCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog = new Dialog(context);
                detailsDialog.setContentView(R.layout.display_task_layout);
                detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                detailsDialog.setCanceledOnTouchOutside(false);
                detailsDialog.show();

                TextView detailsTaskName = detailsDialog.findViewById(R.id.taskName);
                TextView detailsTaskDate = detailsDialog.findViewById(R.id.taskDate);
                TextView detailsTaskTime = detailsDialog.findViewById(R.id.taskTime);
                TextView detailsTaskLocation = detailsDialog.findViewById(R.id.taskLocation);
                TextView detailsTaskRepeat = detailsDialog.findViewById(R.id.taskRepeat);
                TextView detailsTaskPriority = detailsDialog.findViewById(R.id.taskPriority);
                TextView detailsTaskNotes = detailsDialog.findViewById(R.id.taskNotes);

                Button btnDisplayCancel = detailsDialog.findViewById(R.id.btnDisplayCancel);

                detailsTaskName.setText(taskList.get(viewHolder.getAdapterPosition()).getTask_title());
                detailsTaskDate.setText(taskList.get(viewHolder.getAdapterPosition()).getTask_date());
                detailsTaskTime.setText(taskList.get(viewHolder.getAdapterPosition()).getTask_time());
//                detailsTaskLocation.setText(taskList.get(viewHolder.getAdapterPosition()).getTask_());
//                detailsTaskRepeat.setText(taskList.get(viewHolder.getAdapterPosition()).getTask_r());
                detailsTaskPriority.setText(taskList.get(viewHolder.getAdapterPosition()).getPriority());
                detailsTaskNotes.setText(taskList.get(viewHolder.getAdapterPosition()).getNotes());

                btnDisplayCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailsDialog.dismiss();
                    }
                });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.TaskViewHolder holder, final int position) {

        final Task task = taskList.get(position);

        holder.TaskTextView.setText(taskList.get(position).getTask_title());

        holder.addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubTaskDialog = new Dialog(context);
                addSubTaskDialog.setContentView(R.layout.add_subtask_layout);
                addSubTaskDialog.setCanceledOnTouchOutside(false);
                addSubTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addSubTaskDialog.show();

                final EditText addSubtask = addSubTaskDialog.findViewById(R.id.addSubtaskTxt);

                Button addSubtaskBtn = addSubTaskDialog.findViewById(R.id.btnSubtaskAdd);
                Button cancelBtn = addSubTaskDialog.findViewById(R.id.btnSubtaskCancel);



                addSubtaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String subtaskTitle = addSubtask.getText().toString();
                        dialog = new ProgressDialog(context);
                        dialog.setTitle("Please Wait!");
                        dialog.setMessage("Adding Data...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createSubTask(subtaskTitle, taskList.get(position).getTask_id());
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                                DefaultResponse defaultResponse = response.body();
                                Log.e("ADD SUBTASK 1", String.valueOf(defaultResponse.isSucc()));
                                Log.e("ADD SUBTASK 2", String.valueOf(defaultResponse.getMsg()));
                                Log.e("ADD SUBTASK 3", String.valueOf(taskList.get(position).getTask_id()));
                                Log.e("ADD SUBTASK 4", String.valueOf(subtaskTitle));
                                if (response.body().isSucc()){
                                    Toast.makeText(context,defaultResponse.getMsg(), Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    addSubTaskDialog.dismiss();
                                } else  {
                                    Toast.makeText(context,defaultResponse.getMsg(), Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    addSubTaskDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSubTaskDialog.dismiss();
                    }
                });
            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTaskDialog = new Dialog(context);
                shareTaskDialog.setContentView(R.layout.share_task_layout);
                shareTaskDialog.setCanceledOnTouchOutside(false);
                shareTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                shareTaskDialog.show();

                shareEmail = shareTaskDialog.findViewById(R.id.shareTxtEmail);

                shareName = shareTaskDialog.findViewById(R.id.shareTxtTask);
                shareDate = shareTaskDialog.findViewById(R.id.shareTxtDate);
                shareTime = shareTaskDialog.findViewById(R.id.shareTxtTime);

                shareName.setText(taskList.get(holder.getAdapterPosition()).getTask_title());
                shareDate.setText(taskList.get(holder.getAdapterPosition()).getTask_date());
                shareTime.setText(taskList.get(holder.getAdapterPosition()).getTask_time());

                Button btnShare = shareTaskDialog.findViewById(R.id.btnShareTask);
                Button btnCancel = shareTaskDialog.findViewById(R.id.btnShareCancel);

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String taskEmail = shareEmail.getText().toString().trim();
                        String taskName = shareName.getText().toString().trim();
                        String taskDate = shareDate.getText().toString().trim();
                        String taskTime = shareTime.getText().toString().trim();
                        String taskDateTime = taskDate + "," + taskTime;

                        if ((taskEmail.isEmpty()) | (!Patterns.EMAIL_ADDRESS.matcher(taskEmail).matches())) {
                            shareEmail.setError("A valid email address must be entered!");
                            return;
                        } else {
                            shareEmail.setError(null);
                            Intent Email = new Intent(Intent.ACTION_SEND);
                            Email.putExtra(Intent.EXTRA_EMAIL, new String[]{taskEmail});
                            Email.putExtra(Intent.EXTRA_SUBJECT, taskName);
                            Email.putExtra(Intent.EXTRA_TEXT, taskDateTime);

                            Email.setType("message/rfc822");
                            context.startActivity(Intent.createChooser(Email, "Choose app to send email"));}
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareTaskDialog.dismiss();
                    }
                });


            }
        });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = SharedPrefManager.getInstance(context).getUser();
                updateTaskDialog = new Dialog(context);
                updateTaskDialog.setContentView(R.layout.update_task_layout);
                updateTaskDialog.setCanceledOnTouchOutside(false);
                updateTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                updateTaskDialog.show();

                updateId = updateTaskDialog.findViewById(R.id.updateTxtId);
                updateName = updateTaskDialog.findViewById(R.id.updateTxtTask);

                updateDate = updateTaskDialog.findViewById(R.id.updateDatePicker);

                updateTime = updateTaskDialog.findViewById(R.id.updateTimePicker);
                updatePriority = updateTaskDialog.findViewById(R.id.updateSpinnerPriority);
                updateNotes = updateTaskDialog.findViewById(R.id.updateTxtNotes);

                customSpinnerPriority();

                Button btnUpdate = updateTaskDialog.findViewById(R.id.btnUpdateTask);
                Button btnCancel = updateTaskDialog.findViewById(R.id.btnCancel);

                updateId.setText(String.valueOf(taskList.get(holder.getAdapterPosition()).getTask_id()));
                updateName.setText(taskList.get(holder.getAdapterPosition()).getTask_title());
                updateDate.setText(taskList.get(holder.getAdapterPosition()).getTask_date());
                updateTime.setText(taskList.get(holder.getAdapterPosition()).getTask_time());
                updateNotes.setText(taskList.get(holder.getAdapterPosition()).getNotes());

                updateDate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                now.set(Calendar.YEAR, year);
                                now.set(Calendar.MONTH, monthOfYear);
                                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
                                String strDate = format.format(now.getTime());
                                updateDate.setText(strDate);

                                Calendar calendar = Calendar.getInstance();
                                int HOUR = calendar.get(Calendar.HOUR);
                                int MINUTE = calendar.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                                        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        now.set(Calendar.MINUTE, minute);
                                        now.set(Calendar.SECOND, 00);

                                        String text = DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime());
                                        updateTime.setText(text);



                                    }
                                }, HOUR, MINUTE, true);



                                timePickerDialog.show(((AppCompatActivity)context).getFragmentManager(), "Time Picker Dialog");

                            }
                        });

                        datePickerDialog.show(((AppCompatActivity)context).getFragmentManager(), "Date Picker Dialog");

                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String getId = updateId.getText().toString();
                        String getName = updateName.getText().toString().trim();
                        String getDate = updateDate.getText().toString().trim();
                        Log.e("GETDATE", getDate);
                        String getTime = updateTime.getText().toString().trim();
                        Log.e("GETTIME", getTime);
                        String getPriority = updatePriority.getSelectedItem().toString();
                        String getNotes = updateNotes.getText().toString().trim();

                        //notes is nullable
                        HashMap<String, String> params = new HashMap<>();
                        params.put("task_id", getId);
                        params.put("task_title", getName);
                        params.put("task_date", getDate);
                        params.put("task_time", getTime);
                        params.put("priority", getPriority);
                        params.put("notes", getNotes);
                        params.put("user_id", String.valueOf(user.getUser_id()));

                        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_UPDATE_TASK, params, CODE_POST_REQUEST);
                        request.execute();
                        Log.e("TEST 1", "TEST 3");

                        SharedPreferences pref = context.getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Notification_title", getName);
                        editor.putString("Notification_message", getNotes);
                        editor.commit();

                        startAlarm(now);

                        context.startActivity(new Intent(context.getApplicationContext(), context.getClass()));
                        ((Activity)context).finish();

                        updateTaskDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateTaskDialog.dismiss();
                    }
                });


            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = SharedPrefManager.getInstance(context).getUser();
                final Integer task_id = taskList.get(holder.getAdapterPosition()).getTask_id();
                deleteTaskDialog = new Dialog(context);
                deleteTaskDialog.setContentView(R.layout.delete_task_layout);
                deleteTaskDialog.setCanceledOnTouchOutside(false);
                deleteTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteTaskDialog.show();

                Button btnDelete = deleteTaskDialog.findViewById(R.id.btnDeleteTask);
                Button btnCancel = deleteTaskDialog.findViewById(R.id.btnDeleteCancel);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("CHECK", String.valueOf(task.getTask_id()));
                        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_DELETE_TASK + task_id , null, CODE_POST_REQUEST);
                        request.execute();

                        taskList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), taskList.size());
                        notifyDataSetChanged();

                        deleteTaskDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTaskDialog.dismiss();
                    }
                });
            }
        });

        holder.cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getId = String.valueOf(taskList.get(holder.getAdapterPosition()).getTask_id());
                User user = SharedPrefManager.getInstance(context).getUser();
                HashMap<String, String> params = new HashMap<>();
                params.put("task_id", getId);
                params.put("user_id", String.valueOf(user.getUser_id()));

                PerformNetworkRequest request = new PerformNetworkRequest(API.URL_COMPLETE_TASK, params, CODE_POST_REQUEST);
                request.execute();

//                taskList.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
//                notifyItemRangeChanged(holder.getAdapterPosition(), taskList.size());
//                notifyDataSetChanged();
                context.startActivity(new Intent(context.getApplicationContext(), context.getClass()));
                ((Activity)context).finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.subTaskRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(task.getSubTaskList().size());
        Log.e("SIZE", String.valueOf(task.getSubTaskList().size()));

        SubTaskAdapter subTaskAdapter = new SubTaskAdapter(context, task.getSubTaskList());

        holder.subTaskRecyclerView.setLayoutManager(layoutManager);
        holder.subTaskRecyclerView.setAdapter(subTaskAdapter);
        holder.subTaskRecyclerView.setRecycledViewPool(viewPool);
    }

    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int unique = (int) System.currentTimeMillis();

        SharedPreferences pref = context.getSharedPreferences("Notification_Preferences", MODE_PRIVATE);
        String title = pref.getString("Notification_title", "");
        String content = pref.getString("Notification_message", "");

        Log.e("TITLE NOTIFiCATION", title);
        Log.e("CONTENT NOTIFiCATION", content);

        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("Notification_title", title);
        intent.putExtra("Notification_message", content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, unique, intent, 0);

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

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        CardView taskCardView;
        CheckBox cbCompleted;
        TextView TaskTextView;
        ImageView addImageView, shareImageView, editImageView, deleteImageView;
        RecyclerView subTaskRecyclerView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            taskCardView = itemView.findViewById(R.id.taskCardViewID);

            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            TaskTextView = itemView.findViewById(R.id.txtViewTask);
            addImageView = itemView.findViewById(R.id.addSubTask);
            shareImageView = itemView.findViewById(R.id.share);
            editImageView = itemView.findViewById(R.id.edit);
            deleteImageView = itemView.findViewById(R.id.delete);

            subTaskRecyclerView = itemView.findViewById(R.id.subTaskRecyclerView);
        }
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
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
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "FAIL TO LOAD", Toast.LENGTH_SHORT).show();
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

    public void customSpinnerPriority() {
        updatePriority = updateTaskDialog.findViewById(R.id.updateSpinnerPriority);
        priorityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, priorityList) {
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway_reg.ttf");
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

        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updatePriority.setAdapter(priorityAdapter);
        updatePriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
//        String strDate = format.format(c.getTime());
//
//        updateDate.setText(strDate);
//    }
//
//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        updateTime.setText(hourOfDay + ":" + minute);
//    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Task> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(taskListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Task item : taskListAll) {
                    if (item.getTask_title().toLowerCase().contains(filterPattern)) {
                        Log.e("hi", item.getTask_title());

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskList.clear();
            taskList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}

