package com.example.wang.myandroidproject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wang on 2018/5/21.
 */

public class SignUp extends AppCompatActivity{
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private EditText user_id;
    private EditText user_password;
    private Button signup_btn;
    Toast toast;
    private int flag=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        user_id = findViewById(R.id.user_id);
        user_password = findViewById(R.id.password);
        signup_btn=findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
    }
    Runnable runnable = new Runnable() {
        private Connection con = null;// 获得连接对象
        int result;
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
                e.printStackTrace();
                Log.i("RegisterActivity", "连接失败"+e.getMessage()+"");
            }
        }
        private void sqlCon(Connection con1) throws java.sql.SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                Statement sta = con.createStatement(); // 创建语句对象
                String account=user_id.getText().toString().trim();
                String password=user_password.getText().toString().trim();
                String create_time=new Date(System.currentTimeMillis())+"";
                ResultSet rs = sta.executeQuery("SELECT * FROM base_user");
                while(rs.next()) {
                    if(account.equals(""+rs.getString("user_id")+"")){
                        flag=0;
                    }
                }
                if(flag==0){
                    Looper.prepare();
                    toast=Toast.makeText(SignUp.this,"账号已经被注册", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }else if(account.isEmpty()||password.isEmpty()){                               //判断IP输入框是否为空
                    Looper.prepare();
                    toast=Toast.makeText(SignUp.this,"账号或密码不能为空", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }else{
                    result=sta.executeUpdate("INSERT INTO base_user (user_id, password,create_time) VALUES ('"+account+"', '"+password+"','"+create_time+"')");
                }
                if(result>0){
                    Looper.prepare();
                    toast=Toast.makeText(SignUp.this,"注册成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Log.i("RegisterActivity", "INSERT SUCCESS");
                    Intent intent=new Intent(SignUp.this,Login.class);
                    startActivity(intent);
                    finish();
                    Looper.loop();
                }else{
                    Log.i("RegisterActivity", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("RegisterActivity", "MYSQL ERROR"+e.getMessage()+"");
            }
            con.close(); // 关闭所有已经打开的资源
        }
    };
}
