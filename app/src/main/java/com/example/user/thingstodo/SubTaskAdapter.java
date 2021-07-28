package com.example.user.thingstodo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.thingstodo.fragments.DefaultResponse;
import com.example.user.thingstodo.models.SubTask;
import com.example.user.thingstodo.util.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 5/26/2019.
 */

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder> {

    Context mContext;
    List<SubTask> subTaskList;

    private Dialog updateSubTaskDialog, deleteSubTaskDialog;
    ProgressDialog dialog;

    public SubTaskAdapter(Context mContext, List<SubTask> subTaskList) {
        this.mContext = mContext;
        this.subTaskList = subTaskList;
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.subtask_layout, parent, false);
        SubTaskViewHolder viewHolder = new SubTaskViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position) {

        final SubTask subTask = subTaskList.get(position);
        holder.subtaskTitle.setText(subTask.getSubtask());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSubTaskDialog = new Dialog(mContext);
                updateSubTaskDialog.setContentView(R.layout.update_subtask_layout);
                updateSubTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                updateSubTaskDialog.setCanceledOnTouchOutside(false);
                updateSubTaskDialog.show();

                final EditText updateSubtask = updateSubTaskDialog.findViewById(R.id.updateSubtaskTxt);
                Button btnUpdateSubtask = updateSubTaskDialog.findViewById(R.id.btnSubtaskUpdate);
                Button btnCancel = updateSubTaskDialog.findViewById(R.id.btnSubtaskCancel);

                btnUpdateSubtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new ProgressDialog(mContext);
                        dialog.setTitle("Please Wait!");
                        dialog.setMessage("Updating Data...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        String updateSubTask = updateSubtask.getText().toString();
                        Call<SubTaskResponse> call = RetrofitClient.getInstance().getApi().updateSubTask(updateSubTask, subTask.getSubtask_id());
                        call.enqueue(new Callback<SubTaskResponse>() {
                            @Override
                            public void onResponse(Call<SubTaskResponse> call, Response<SubTaskResponse> response) {

                                SubTaskResponse subTaskResponse = response.body();
                                if (subTaskResponse.isSuccess()){

                                    dialog.dismiss();
                                    updateSubTaskDialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    updateSubTaskDialog.dismiss();
                                }
                            }
                            @Override
                            public void onFailure(Call<SubTaskResponse> call, Throwable t) {
                            }
                        });
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateSubTaskDialog.dismiss();
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubTaskDialog = new Dialog(mContext);
                deleteSubTaskDialog.setContentView(R.layout.delete_subtask_layout);
                deleteSubTaskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteSubTaskDialog.setCanceledOnTouchOutside(false);
                deleteSubTaskDialog.show();

                Button btnDeleteSubtask = deleteSubTaskDialog.findViewById(R.id.btnSubtaskDelete);
                Button btnCancel = deleteSubTaskDialog.findViewById(R.id.btnSubtaskCancelDelete);

                btnDeleteSubtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new ProgressDialog(mContext);
                        dialog.setTitle("Please Wait!");
                        dialog.setMessage("Deleting Data...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteSubTask(subTask.getSubtask_id());
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse defaultResponse = response.body();
                                if (defaultResponse.isSucc()){
                                    dialog.dismiss();
                                    deleteSubTaskDialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    deleteSubTaskDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteSubTaskDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return subTaskList.size();
    }

    public class SubTaskViewHolder extends RecyclerView.ViewHolder {

        TextView subtaskTitle;
        ImageView edit, delete;

        public SubTaskViewHolder(View itemView) {
            super(itemView);

            subtaskTitle = itemView.findViewById(R.id.subTaskTitle);
            edit = itemView.findViewById(R.id.subTaskEdit);
            delete = itemView.findViewById(R.id.subTaskDelete);
        }
    }
}
