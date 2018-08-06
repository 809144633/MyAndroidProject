package com.example.wang.myandroidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willam on 2018/5/19.
 */

public class task_details extends Activity {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private Toast toast;
    private TextView price;
    private TextView t_title;
    private TextView message;
    private String Itemtitle;
    private String ItemContent;
    private String Price;
    private Button btn_back;
    private Button I_want;
    private Button delete;//删除发布的信息。
    private TextView edit_address;
    private Context mcontent;
    private Connection con = null;// 获得连接对象
    private int result;
    private String login_id;//登陆用户用户名。
    private String task_user_id;
    private String t_user_id;//任务发布者用户名。
    private String product_id;
    private String p_id;
    private String Itemaddress;
    private int flag=0;
    private int f;
    private TextView task_id;//显示用户名控件。
    private ArrayList<Map<String,Object>> li2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;//全局变量获得user_id。
    private String str;
    @SuppressLint("HandlerLeak")
    private Handler mhandle=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            price.setText(Price);
            t_title.setText(Itemtitle);
            message.setText(ItemContent);
            task_id.setText(task_user_id);
            t_user_id=task_user_id;
            p_id=product_id;
            f=flag;
            edit_address.setText(Itemaddress);
            if_own_delete();
            if_own_want();
//            if (login_id.equals(t_user_id)){
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(task_details.this,"您无法接收自己的订单",Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//            }
//            else {
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        new Thread(mrunnabel_delete).start();
//                        Toast.makeText(task_details.this,"接单成功！请联系雇主了解后续",Toast.LENGTH_SHORT).show();
////                        Intent intent=new Intent(task_details.this,MainActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
//                });
//            }

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        pref=getSharedPreferences("data",MODE_PRIVATE);
        login_id=pref.getString("user_id","");
        task_id=findViewById(R.id.task_id);
        price=findViewById(R.id.price);
        t_title=findViewById(R.id.t_title);
        message=findViewById(R.id.message);
        btn_back=findViewById(R.id.btnBack);
        delete=findViewById(R.id.delete);
        I_want=findViewById(R.id.I_want);
        edit_address=findViewById(R.id.tv_address);
        mcontent=this;
        li2=new ArrayList<Map<String, Object>>();
        new Thread(mrunnable_goods_details).start();
//        I_want.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toast.makeText(task_details.this,"接单成功，请在个人中心-我的接单中查看并联系卖家了解后续",Toast.LENGTH_SHORT).show();
//                new Thread(taskrunnable).start();
//                Intent intent=new Intent(task_details.this,User.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task_details.this.finish();
            }
        });
    }
    Runnable mrunnable_goods_details=new Runnable() {
        private Connection con = null;// 获得连接对象
        private PreparedStatement ps=null;//操作整合sql语句的对象
        private ResultSet rs=null;//查询结果的集合
        String sql;
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
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("task_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                String invisible_id=pref.getString("invisible_id","");
                sql="SELECT * FROM task WHERE id='"+invisible_id+"' and destroy_time is NULL";
                Log.i("dsafsadf",sql);
                if (con!=null&&!con.isClosed()){
                    ps= (PreparedStatement) con.prepareStatement(sql);
                    if(ps!=null){
                        rs=ps.executeQuery();
                        if (rs!=null){
                            while (rs.next()){
                               /* BigDecimal beichushu=new BigDecimal(sPrice);
                                BigDecimal chushu=new BigDecimal(100000000);*/
                               /* BigDecimal result=beichushu.divide(chushu,new MathContext(4));*/
                                /*double sPrice=Double.valueOf(rs.getString("price")) ;*/
                                Itemtitle=rs.getString("title");
                                ItemContent=rs.getString("content");
                                Price=rs.getInt("price")+"";
                                task_user_id=rs.getString("user_id");
                                product_id=rs.getString("id");
                                Itemaddress=rs.getString("address");
                                flag=1;

                            }
                        }
                    }
                }else{
                    Log.i("task_details", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("task_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            mhandle.sendEmptyMessage(0);
            con.close(); // 关闭所有已经打开的资源
        }
    };
    Runnable taskrunnable=new Runnable() {
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
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("task_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                String create_time= String.valueOf(Calendar.getInstance().getTimeInMillis());
                String str=  pref.getString("user_id","");
                result=sta.executeUpdate("INSERT INTO user_task(user_id,task_id,create_time)VALUES('"+str+"',"+p_id+",'"+create_time+"')");
                if (result>0){
                    Log.i("task_details", "INSERT success");
                    new Thread(arunnabel_delete).start();
                }
                else{
                    Log.i("task_details", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("task_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            mhandle.sendEmptyMessage(0);
            con.close(); // 关闭所有已经打开的资源

        }
    };
    Runnable mrunnabel_delete=new Runnable() {
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
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("task_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                BigInteger destroy_time= BigInteger.valueOf(Calendar.getInstance().getTimeInMillis());
                result=sta.executeUpdate("UPDATE task SET destroy_time="+destroy_time+" WHERE id="+p_id+"");
                if (result>0){
                    Log.i("task_details", "delete success");
                }
                else{
                    Log.i("task_details", "delete FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("task_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            mhandle.sendEmptyMessage(0);
            con.close();
        }
    };
    Runnable arunnabel_delete=new Runnable() {
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
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("task_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                result=sta.executeUpdate("UPDATE task SET flag="+f+" WHERE id="+p_id+"");
                if (result>0){
                    Log.i("task_details", "delete success");
                }
                else{
                    Log.i("task_details", "delete FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("task_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            mhandle.sendEmptyMessage(0);
            con.close();
        }
    };
    public void if_own_delete(){
        if (login_id.equals(t_user_id)){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(task_details.this,"删除成功！",Toast.LENGTH_SHORT).show();
//                    new Thread(mrunnabel_delete).start();
//                    Intent intent=new Intent(task_details.this,goods_changelist.class);
//                    startActivity(intent);
                }
            });
        }
        else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(task_details.this,"您非该任务的发布者，无法对此任务进行删除操作！",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void if_own_want(){
        if (login_id.equals(t_user_id)){
            I_want.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(task_details.this,"您无法购买自己的商品",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            I_want.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(task_details.this,"下单成功，请在个人中心-我买到的中查看并联系卖家了解后续",Toast.LENGTH_SHORT).show();
                    new Thread(mrunnabel_delete).start();
                    Intent intent=new Intent(task_details.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
