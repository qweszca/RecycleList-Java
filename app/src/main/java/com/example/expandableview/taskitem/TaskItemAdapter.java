package com.example.expandableview.taskitem;


import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.expandableview.ExPandableViewActivity;
import com.example.expandableview.R;
import com.example.expandableview.po.Task;
import com.example.expandableview.po.TaskItem;
import com.example.expandableview.utils.MyUtils;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
    private ArrayList<TaskItem> mtaskItemList;
    private int parentPosition;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View taskItemView;
        TextView taskItemName;
        TextView taskItemTime;
        TextView startTask;
        TextView deleteTask;
        RelativeLayout itemLayout;
        TextView itemBottomBar;
        public ViewHolder(View view) {
            super(view);
            taskItemView = view;

            itemLayout=(RelativeLayout)view.findViewById(R.id.item_layout);

            taskItemName = view.findViewById(R.id.task_item_name);
            taskItemTime = view.findViewById(R.id.task_item_time);
            itemBottomBar=view.findViewById(R.id.item_bottom_bar);

            startTask=view.findViewById(R.id.start_task);
            deleteTask=view.findViewById(R.id.del_task_item);
        }
    }

    //添加
    public void addTaskItem(TaskItem newTask){
        mtaskItemList.add(newTask);
        notifyDataSetChanged();

        newTask.save();

    }
    public void deleteAllItem(int taskId){
        LitePal.deleteAll(TaskItem.class,"taskId=?",taskId+"");
    }
    //删除
    public void deleteTaskItem(int pos){
        mtaskItemList.remove(pos);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //GradientDrawable gd =(GradientDrawable) holder.itemLayout.getBackground();
        //gd.setColor(MyUtils.randomColor());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TaskItem taskItem = mtaskItemList.get(position);
        holder.taskItemName.setText(taskItem.getItemName());
        holder.taskItemTime.setText(taskItem.getItemTime());
        if(position==mtaskItemList.size()-1){
            holder.itemBottomBar.setBackgroundColor(Color.rgb(246,247,249));
        }
        else {
            holder.itemBottomBar.setBackgroundColor(Color.rgb(204,199,199));
        }
        holder.startTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                TaskItem taskItem = mtaskItemList.get(position);
                Toast.makeText(view.getContext(), "开始"+ taskItem.getItemName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                deleteTaskItem(position);
                taskItem.delete();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mtaskItemList.size();
    }

    public TaskItemAdapter(int pos) {
        mtaskItemList=(ArrayList<TaskItem>) LitePal.where("taskId=?",pos+"").find(TaskItem.class);
        parentPosition=pos;
    }

}