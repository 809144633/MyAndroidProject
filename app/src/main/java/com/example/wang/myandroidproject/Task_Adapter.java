package com.example.wang.myandroidproject;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.ViewHolder>{
    private List<Task> taskList;
    public Task_Adapter(List<Task> taskList) {
        this.taskList=taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task=taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.price.setText(task.getPrice()+"");
        holder.text.setText(task.getShort_des());
        holder.time.setText(task.getTime());
        holder.sort.setText(task.getSort());
        holder.invisible_id.setText(task.getInvisible_id()+"");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;
        TextView sort;
        TextView price;
        TextView time;
        TextView invisible_id;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.item_title);
            text=itemView.findViewById(R.id.item_text);
            sort=itemView.findViewById(R.id.item_sort);
            price=itemView.findViewById(R.id.item_price);
            time=itemView.findViewById(R.id.item_time);
            invisible_id=itemView.findViewById(R.id.invisible_id);
        }
    }
}
