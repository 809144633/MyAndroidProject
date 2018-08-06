package com.example.wang.myandroidproject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2018/5/13.
 */

public class Message_list extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String[] title={"活动消息","通知","tb-809144633"};
    private String[] short_des={"全新夏装促销","实人认证","确认交易"};
    private String[] time={"10","20","45"};
    private int[] pic={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private SimpleAdapter sim_adapter;
    private List<Map<String,Object>> datalist;
    private ListView list_view;
    private Button btn_personal;
    private Button btn_message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
        list_view=findViewById(R.id.listview_for_message);
        btn_personal=findViewById(R.id.personal);
        datalist=new ArrayList<>();
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.message_item,
                new String[]{"title", "short_des","time","pic"},
                new int[]{R.id.title_for_message, R.id.short_description_for_message,R.id.time_for_message,R.id.pic_for_message});
        list_view.setAdapter(sim_adapter);
        /*
            底部导航栏监听
         */
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Message_list.this,User.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Message_list.this,Message_list.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private List<Map<String,Object>> getData() {
        for(int i=0;i<title.length;i++){
            Map<String, Object>map = new HashMap<String, Object>();
            map.put("title",title[i]);
            map.put("short_des",short_des[i]);
            map.put("time",time[i]+"分钟");
            map.put("pic",pic[i]);
            datalist.add(map);
        }
        return datalist;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

