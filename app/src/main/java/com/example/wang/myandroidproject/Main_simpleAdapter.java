package com.example.wang.myandroidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Willam on 2018/5/12.
 */
public class Main_simpleAdapter extends SimpleAdapter {
    private LinkedList<task_messgae> mData;
    private Context mContext;
    public Main_simpleAdapter(Context context, ArrayList data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_item,parent,false);
            holder = new ViewHolder();
            holder.price = (TextView) convertView.findViewById(R.id.item_price);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.content = (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.price.setText(""+mData.get(position).getPrice());
        holder.title.setText(mData.get(position).getItemtitle());
        holder.content.setText(mData.get(position).getItemContent());

        return convertView;
    }
   static class ViewHolder{
        public TextView title;
        public TextView content;
        public TextView price;
    }
}

