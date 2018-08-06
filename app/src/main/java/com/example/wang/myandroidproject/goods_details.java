package com.example.wang.myandroidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class goods_details extends Activity {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private TextView price;
    private TextView t_title;
    private TextView message;
    private String Itemtitle;
    private String ItemContent;
    private String Price;
    private Button btnback;
    private Button I_want_goods;
    private Button delete;//删除发布的信息。
    private TextView goods_id;//显示用户名控件。
    private String login_id;//登陆用户用户名。
    private String product_user_id;
    private String p_user_id;//任务发布者用户名。
    private TextView tv_address;
    private int product_id;//商品ID。
    private int p_id;
    private Connection con = null;// 获得连接对象
    private int result;
    private int flag=0;
    private int f;
    private String Itemaddress;
    SharedPreferences pref;
    SharedPreferences.Editor editor;//全局变量获得user_id。
    private String str;
    private ArrayList<Map<String,Object>> li2;
    @SuppressLint("HandlerLeak")
    private Handler mhandle=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            price.setText(Price);
            t_title.setText(Itemtitle);
            message.setText(ItemContent);
            goods_id.setText(product_user_id);
            p_user_id=product_user_id;
            p_id=product_id;
            f=flag;
            tv_address.setText(Itemaddress);
            if_own_delete();
            if_own_want();
//            if (login_id.equals(p_user_id)){
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(goods_details.this,"您无法购买自己的商品",Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//            }
//            else {
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new Thread(mrunnabel_delete).start();
//                        Toast.makeText(goods_details.this,"下单成功！请联系卖家了解后续",Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(goods_details.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);
        new Thread(mrunnable_goods_details).start();
        pref=getSharedPreferences("data",MODE_PRIVATE);
        editor=pref.edit();
        str=pref.getString("user_id","");
        btnback=findViewById(R.id.btnBack);
        delete=findViewById(R.id.delete);
        price=findViewById(R.id.price);
        t_title=findViewById(R.id.t_title);
        message=findViewById(R.id.message);
        I_want_goods=findViewById(R.id.I_want_goods);
        tv_address=findViewById(R.id.tv_address);
        goods_id=findViewById(R.id.goods_id);
        login_id=str;
        li2=new ArrayList<Map<String, Object>>();
//        I_want_goods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toast.makeText(goods_details.this,"下单成功，请在个人中心-我买到的中查看并联系卖家了解后续",Toast.LENGTH_SHORT).show();
//                new Thread(mrunnable_I_want_goods).start();
//                Intent intent=new Intent(goods_details.this,User.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goods_details.this.finish();
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
                Log.i("goods_details", "连接失败" + e.getMessage() + "");
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
                sql="SELECT * FROM product WHERE id='"+invisible_id+"' and destroy_time is NULL";
                Log.i("asdfasdfasf",sql);
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
                                product_id=rs.getInt("id");
                                product_user_id=rs.getString("user_id");
                                Itemaddress=rs.getString("address");
                            }
                        }
                    }
                }else{
                    Log.i("goods_details", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("goods_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            mhandle.sendEmptyMessage(0);
            con.close(); // 关闭所有已经打开的资源
        }
    };
    Runnable mrunnable_I_want_goods=new Runnable() {
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
                Log.i("goods_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                String create_time= String.valueOf(Calendar.getInstance().getTimeInMillis());
                result=sta.executeUpdate("INSERT INTO user_product(user_id,product_id,create_time)VALUES('"+login_id+"',"+product_id+",'"+create_time+"')");
                if (result>0){
                    flag=1;
                    Log.i("goods_details", "INSERT success");
                    new Thread(arunnabel).start();
                }
                else{
                    Log.i("goods_details", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("goods_details", "MYSQL ERROR"+e.getMessage()+"");
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
                    Log.i("goods_details", "连接失败" + e.getMessage() + "");
                }
            }
            private void sqlCon(Connection con1) throws SQLException {
                try { // 监控异常
                    Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                    Connection con;// 获得连接对象
                    con = DriverManager.getConnection(strCon, strUser, strPwd);
                    System.out.print("连接成功");
                    Statement sta = con.createStatement(); // 创建语句对象
                    String destroy_time= String.valueOf(Calendar.getInstance().getTimeInMillis());
                    result=sta.executeUpdate("UPDATE product SET destroy_time=('"+destroy_time+"') WHERE id="+p_id+"");
                   if (result>0){
                       Log.i("goods_details", "delete success");
                   }
                    else{
                        Log.i("goods_details", "delete FAIL");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("goods_details", "MYSQL ERROR"+e.getMessage()+"");
                }
                mhandle.sendEmptyMessage(0);
                con.close();

            }
        };
    Runnable arunnabel=new Runnable() {
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
                Log.i("goods_details", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                String destroy_time= String.valueOf(Calendar.getInstance().getTimeInMillis());
                result=sta.executeUpdate("UPDATE product SET flag=('"+f+"') WHERE id="+p_id+"");
                if (result>0){
                    Log.i("goods_details", "delete success");
                }
                else{
                    Log.i("goods_details", "delete FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("goods_details", "MYSQL ERROR"+e.getMessage()+"");
            }
            con.close();
        }
    };
    public void if_own_delete(){
        if (login_id.equals(p_user_id)){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(goods_details.this,"删除成功！",Toast.LENGTH_SHORT).show();
                    new Thread(mrunnabel_delete).start();
                    Intent intent=new Intent(goods_details.this,goods_changelist.class);
                    startActivity(intent);
                    }
                });
            }
            else {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(goods_details.this,"您非该任务的发布者，无法对此任务进行删除操作！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }
    public void if_own_want(){
        if (login_id.equals(p_user_id)){
            I_want_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(goods_details.this,"您无法购买自己的商品",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
            I_want_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(goods_details.this,"下单成功，请在个人中心-我买到的中查看并联系卖家了解后续",Toast.LENGTH_SHORT).show();
                        new Thread(mrunnabel_delete).start();
                        Intent intent=new Intent(goods_details.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
    }
}
