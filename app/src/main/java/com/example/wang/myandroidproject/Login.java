package com.example.wang.myandroidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by wang on 2018/5/7.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private EditText user_phone;
    private EditText user_password;
    private Button login_btn;
    private Button signup_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        user_phone = findViewById(R.id.user_phone);
        user_password = findViewById(R.id.password);
        signup_btn=findViewById(R.id.signup_btn);
        login_btn=findViewById(R.id.login_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });
    }
    Runnable runnable = new Runnable() {
        private Connection con = null;// 获得连接对象
        int flag=0;
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
                Log.i("LoginActivity", "连接失败"+e.getMessage()+"");
            }
        }
        private void sqlCon(Connection con1) throws java.sql.SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                Statement sta = con.createStatement(); // 创建语句对象
                ResultSet rs = sta.executeQuery("SELECT * FROM base_user WHERE destroy_time is NULL");// 执行SQL语句
                String account=user_phone.getText().toString();
                String password=user_password.getText().toString();
                if(account.isEmpty()||password.isEmpty())
                {                                  //判断输入框是否为空
                    Looper.prepare();
                    Toast toast=Toast.makeText(Login.this,"账号或密码不能为空", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }
                while (rs.next()) { // 循环将结果集游标往下移动，到达末尾返回false
                    Log.d("LoginActivity", ""+rs.getString("user_id")+"");
                    Log.d("LoginActivity", ""+rs.getString("password")+"");
                    if (account.equals(""+rs.getString("user_id")+"")
                            &&password.equals(""+rs.getString("password")+""))
                    {
                        Log.d("LoginActivity", "登陆成功");
                        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("user_id",account);
                        editor.apply();
                        Looper.prepare();
                        Toast toast=Toast.makeText(Login.this,"登录成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        flag=1;
                        Log.i("wangziju",pref.getString("user_id",""));
                        Looper.loop();
                    }
                }
                if(flag==0){
                    Looper.prepare();
                    Toast toast=Toast.makeText(Login.this,"账号或密码错误", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                    Log.d("LoginActivity", "账号或密码错误");
                }
                rs.close();
                sta.close();
                con.close(); // 关闭所有已经打开的资源
            } catch (ClassNotFoundException e ) {
                e.printStackTrace();
                Log.i("LoginActivity", "连接失败:"+e.getMessage()+"");
            } finally {
                if (con1 != null)
                    try {
                        con1.close();
                        Log.i("LoginActivity", "连接关闭:");
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                        Log.i("LoginActivity", "连接失败:"+sqle.getMessage()+"");
                    }
            }
        }
    };
    public void onBackPressed() {
        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
        MyIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(MyIntent);
    }

    @Override
    public void onClick(View view) {


    }
}
