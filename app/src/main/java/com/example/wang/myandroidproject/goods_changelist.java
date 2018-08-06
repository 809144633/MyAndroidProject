package com.example.wang.myandroidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2018/5/27.
 */

public class goods_changelist extends AppCompatActivity {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private String array[] = new String[]{"校园任务", "二手商品"};
    private Context mContext;
    private ListView list2;
    private Button btn_personal;
    private Button btn_index;
    private ArrayList<Map<String,Object>> li2;
    private Button btnback2,btfirst;
    private ImageButton btn_add;
    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            SimpleAdapter mAdapter= new SimpleAdapter(mContext,li2,R.layout.main_item,new String[]{"title","content","sort","price","invisible_id"},new int[]{R.id.item_title,R.id.item_text,R.id.item_sort,R.id.item_price,R.id.invisible_id});
            list2.setAdapter(mAdapter);
        }};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_changelist);
        new Thread(mrunnable_goods).start();
        btnback2=findViewById(R.id.btnBack2);
        btfirst=findViewById(R.id.btn_index);
        btn_personal=findViewById(R.id.personal);
        btn_index=findViewById(R.id.btn_index);
        btn_add=findViewById(R.id.btn_add);
        list2=findViewById(R.id.list3);
        mContext=this;
        li2=new ArrayList<Map<String, Object>>();
        btnback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goods_changelist.this.finish();
            }
        });
        btfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,User.class);
                startActivity(intent);
                finish();
            }
        });
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad =new AlertDialog.Builder(goods_changelist.this).setTitle("请选择")
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListView lw = ((AlertDialog) dialogInterface).getListView();
                                Object checkedItem = lw.getAdapter().getItem(i);
                                dialogInterface.dismiss();
                                String xz=(String) checkedItem;
                                /*toast.makeText(MainActivity.this,xz,Toast.LENGTH_SHORT).show();*/  //测试xz的值。
                                if (xz.equals("校园任务")){
                                    Intent intent = new Intent(mContext, Publish_task.class);
                                    startActivity(intent);}
                                else{
                                    Intent intent = new Intent(mContext, Publish_product.class);
                                    startActivity(intent);
                                }
                            }

                        });
                AlertDialog dialog = ad.create();
                dialog.show();
            }
        });
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map;
                map = (HashMap<String,Object>)parent.getItemAtPosition(position);
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("invisible_id", map.get("invisible_id").toString());
                editor.apply();
                Intent intent = new Intent(mContext, goods_details.class);
                startActivity(intent);
            }
        });
    }
    Runnable mrunnable_goods=new Runnable() {
        private Connection con = null;// 获得连接对象
        private PreparedStatement ps=null;//操作整合sql语句的对象
        private ResultSet rs=null;//查询结果的集合
        @Override
        public void run() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(strCon, strUser, strPwd);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                sqlCon(con);    //测试数据库连接
            } catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("Publish_task", "连接失败" + e.getMessage() + "");
            }
        }

        private void sqlCon(Connection con1) throws java.sql.SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                String sql="select * from product WHERE flag=0 and destroy_time is NULL ORDER BY create_time DESC";
                if (con!=null&&!con.isClosed()){
                    ps= (PreparedStatement) con.prepareStatement(sql);
                    if(ps!=null){
                        rs=ps.executeQuery();
                        if (rs!=null){
                            while (rs.next()){
                                task_messgae task=new task_messgae();
                                task.setItemtitle(rs.getString("title"));
                                task.setItemContent(rs.getNString("content"));
                                task.setSort(rs.getString("sort"));
                                task.setPrice(rs.getInt("price"));
                                task.setId(rs.getInt("id"));
                               /* BigDecimal beichushu=new BigDecimal(sPrice);
                                BigDecimal chushu=new BigDecimal(100000000);*/
                               /* BigDecimal result=beichushu.divide(chushu,new MathContext(4));*/
                                /*double sPrice=Double.valueOf(rs.getString("price")) ;*/
                                String Itemtitle=task.getItemtitle();
                                String ItemContent=task.getItemContent();
                                String Sort=task.getSort();
                                int id=task.getId();
                                int Price=task.getPrice();
                                Map<String,Object> itemData = new HashMap<String,Object>();
                                itemData.put("title",Itemtitle);
                                itemData.put("content",ItemContent);
                                itemData.put("sort",Sort);
                                itemData.put("price",Price);
                                itemData.put("invisible_id",id);
                                li2.add(itemData);
                            }
                        }
                    }
                }
                else{
                    Log.i("Publish_task", "INSERT FAIL");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.i("Publish_task", "MYSQL ERROR"+e.getMessage()+"");
            }
            mHandler.sendEmptyMessage(0);
            con.close(); // 关闭所有已经打开的资源
        }
    };
}
