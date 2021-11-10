package com.example.expandableview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;


import android.widget.*;
import com.example.expandableview.po.Task;
import com.example.expandableview.po.TaskItem;
import com.example.expandableview.recycleritemanim.ExpandableViewHoldersUtil;
import com.example.expandableview.taskitem.TaskItemAdapter;
import com.example.expandableview.utils.MyUtils;
import org.litepal.LitePal;


import java.util.ArrayList;
import java.util.List;


public class ExPandableViewActivity extends AppCompatActivity {
    private ExpandableViewHoldersUtil.KeepOneHolder<ViewHolder> keepOne;
    private List<Task> taskList;

    private View view;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        //设置bar
        Toolbar toolbar=(Toolbar) findViewById(R.id.task_bar);
        setSupportActionBar(toolbar);

        ExpandableViewHoldersUtil.getInstance().init().setNeedExplanedOnlyOne(false);
        initData();
        initView();
    }

    private void initData() {
        //数据就不造了，xml里面直接显示好了
        taskList=LitePal.findAll(Task.class);

    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.id_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //清空记录展开还是关闭的缓存数据
        myAdapter=new MyAdapter(taskList);
        ExpandableViewHoldersUtil.getInstance().resetExpanedList();
        recyclerView.setAdapter(myAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_toolbar,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_task:
                view = LayoutInflater.from(ExPandableViewActivity.this).inflate(R.layout.add_task_dialog, null);
                AlertDialog dialog = new AlertDialog.Builder(ExPandableViewActivity.this)
                .setTitle("添加任务：")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText nameEdit=(EditText) view.findViewById(R.id.set_task_name);
                                EditText timeEdit=(EditText) view.findViewById(R.id.set_task_time);
                                String nameText=nameEdit.getText().toString();
                                String timeText=timeEdit.getText().toString();
                                if(nameText.isEmpty()||timeText.isEmpty()){
                                    Toast.makeText(ExPandableViewActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Task tasks=new Task();
                                tasks.setName(nameText);
                                tasks.setTime(timeText+"天");
                                tasks.save();
                                myAdapter.addTask(tasks);

                            }

                        })
                .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            default:
                    break;
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        public MyAdapter(List<Task>list) {
            taskList=list;
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
        //添加
        public void addTask(Task newTask){
            taskList.add(newTask);
            notifyDataSetChanged();
        }
        //删除
        public void deleteTask(int pos){
            taskList.remove(pos);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(ExPandableViewActivity.this).inflate(R.layout.item_user_concern_layout, viewGroup, false);
            ViewHolder holder=new ViewHolder(view);
            GradientDrawable gd =(GradientDrawable) holder.taskLayout.getBackground();
            gd.setColor(MyUtils.randomColor());
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
            //设置任务参数
            final Task task=taskList.get(position);
            viewHolder.tvTitle.setText(task.getName());
            viewHolder.taskTime.setText(task.getTime());
            //viewHolder.taskBar.setBackgroundColor(MyUtils.randomColor());

            LinearLayoutManager layoutManager=new LinearLayoutManager(ExPandableViewActivity.this);
            viewHolder.taskItemList.setLayoutManager(layoutManager);
            final TaskItemAdapter itemAdapter=new TaskItemAdapter(task.getId());
            viewHolder.taskItemList.setAdapter(itemAdapter);

            keepOne.bind(viewHolder, position);
            //点击事件
            viewHolder.taskLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(ExpandableViewHoldersUtil.isExpaned(position)){
//                        viewHolder.contentTv.setMaxLines(3);
//                    }else {
//                        viewHolder.contentTv.setMaxLines(100);
//                    }
                    keepOne.toggle(viewHolder);
                    Log.d("Pos",position+"");
                }
            });

            viewHolder.lvArrorwBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keepOne.toggle(viewHolder);
                }
            });

            viewHolder.addItemTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View views) {
                    view = LayoutInflater.from(ExPandableViewActivity.this).inflate(R.layout.add_task_item_dialog, null);
                    AlertDialog dialog = new AlertDialog.Builder(ExPandableViewActivity.this)
                            .setTitle("添加任务项目：")
                            .setView(view)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText nameEdit=(EditText) view.findViewById(R.id.set_task_name);
                                    EditText timeEdit=(EditText) view.findViewById(R.id.set_task_time);
                                    String nameText=nameEdit.getText().toString();
                                    String timeText=timeEdit.getText().toString();
                                    if(nameText.isEmpty()||timeText.isEmpty()){
                                        Toast.makeText(ExPandableViewActivity.this,"任务项目和时间不能为空！",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    TaskItem taskItem=new TaskItem();
                                    taskItem.setItemName(nameText);
                                    taskItem.setItemTime(timeText+"mins");
                                    taskItem.setTaskId(task.getId());
                                    itemAdapter.addTaskItem(taskItem);
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            });
            viewHolder.delTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.deleteTask(position);
                    itemAdapter.deleteAllItem(task.getId());
                    task.delete();

                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ExpandableViewHoldersUtil.Expandable {
        TextView tvTitle;
        TextView taskTime;
        ImageView arrowImage;
        ImageView addItemTask;
        ImageView delTask;
        LinearLayout lvArrorwBtn;
        LinearLayout lvLinearlayout;
        RecyclerView taskItemList;
        //TextView taskBar;
        RelativeLayout taskLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.item_user_concern_title);
            lvLinearlayout = itemView.findViewById(R.id.item_user_concern_link_layout);
            lvArrorwBtn = itemView.findViewById(R.id.item_user_concern_arrow);
            arrowImage = itemView.findViewById(R.id.item_user_concern_arrow_image);
            taskItemList =(RecyclerView) itemView.findViewById(R.id.task_item_list);
            addItemTask=itemView.findViewById(R.id.task_item_add);
            delTask=itemView.findViewById(R.id.task_remove);
            taskTime=itemView.findViewById(R.id.item_user_time);
            //taskBar=itemView.findViewById(R.id.task_bar);
            taskLayout=itemView.findViewById(R.id.task_layout);
            keepOne = ExpandableViewHoldersUtil.getInstance().getKeepOneHolder();

            lvLinearlayout.setVisibility(View.GONE);
            lvLinearlayout.setAlpha(0);
        }

        @Override
        public View getExpandView() {
            return lvLinearlayout;
        }

        @Override
        public void doCustomAnim(boolean isOpen) {
            if (isOpen) {
                ExpandableViewHoldersUtil.getInstance().rotateExpandIcon(arrowImage, 180, 0);
            } else {
                ExpandableViewHoldersUtil.getInstance().rotateExpandIcon(arrowImage, 0, 180);
            }
        }
    }

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, ExPandableViewActivity.class);
        context.startActivity(intent);
    }

}
