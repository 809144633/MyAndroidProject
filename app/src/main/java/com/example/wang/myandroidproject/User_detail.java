package com.example.wang.myandroidproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wang on 2018/5/9.
 */
public class User_detail extends AppCompatActivity implements View.OnClickListener{
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private String[] sexArry = new String[] { "女", "男" };
    private Button btn_birth;
    private TextView tv_birth;
    private Button btn_sex;
    private TextView tv_sex;
    private Button btn_phone;
    private TextView tv_phone;
    private Button btn_address;
    private TextView tv_address;
    private Button btn_name;
    private TextView tv_name;
    private Button btn_save;
    private String un;
    private String ad;
    private String bir;
    private String ph;
    private String s;
    @SuppressLint("HandlerLeak")
    private Handler mHanler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            tv_name.setText(un);
            tv_address.setText(ad);
            tv_birth.setText(bir);
            tv_phone.setText(ph);
            tv_sex.setText(s);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);
        btn_birth=findViewById(R.id.birth);
        tv_birth=findViewById(R.id.textView_birth);
        btn_sex=findViewById(R.id.sex);
        tv_sex=findViewById(R.id.textView_sex);
        btn_phone=findViewById(R.id.phone_number);
        tv_phone=findViewById(R.id.textView_phone);
        btn_address=findViewById(R.id.address);
        tv_address=findViewById(R.id.textView_address);
        btn_name=findViewById(R.id.button_name);
        tv_name=findViewById(R.id.user_name);
        btn_save=findViewById(R.id.save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(save_info).start();
            }
        });
        /*
            性别选择器
         */
        btn_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSexChooseDialog();
            }
        });

        /*
            生日选择器
         */
        btn_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(User_detail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_birth.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2000,1,2).show();
            }
        });

        /*
            联系方式填写
         */
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPhoneDialog();
            }
        });
        /*
            联系地址填写
         */
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputAddressDialog();
            }
        });
        /*
            昵称填写
         */
        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNameDialog();
            }
        });
        new Thread(runnable).start();//加载数据
    }

    /*
        性别选择函数
     */
    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                tv_sex.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder.show();// 让弹出框显示
    }

    private void inputAddressDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_TEXT);
        inputServer.setFocusable(true);
        inputServer.setFocusableInTouchMode(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inputServer).setNegativeButton(
                getString(R.string.取消), null);
        builder.setPositiveButton(getString(R.string.确定),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        tv_address.setText(inputName);
                    }
                });
        builder.show();
    }

    private void inputNameDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_TEXT);
        inputServer.setFocusable(true);
        inputServer.setFocusableInTouchMode(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inputServer).setNegativeButton(
                getString(R.string.取消), null);
        builder.setPositiveButton(getString(R.string.确定),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        tv_name.setText(inputName);
                    }
                });
        builder.show();
    }

    private void inputPhoneDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
        inputServer.setFocusable(true);
        inputServer.setFocusableInTouchMode(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inputServer).setNegativeButton(
                getString(R.string.取消), null);
        builder.setPositiveButton(getString(R.string.确定),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        tv_phone.setText(inputName);
                    }
                });
        builder.show();
    }
    Runnable runnable = new Runnable() {
        private Connection con = null;// 获得连接对象
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
            SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
            String user_id= pref.getString("user_id","");
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                Statement sta = con.createStatement(); // 创建语句对象
                ResultSet rs = sta.executeQuery("SELECT * FROM base_user WHERE user_id=('"+user_id+"')");// 执行SQL语句
                while (rs.next()) { // 循环将结果集游标往下移动，到达末尾返回false
                    Log.d("UserActivity", ""+rs.getString("user_id")+"");
                    Log.d("UserActivity", ""+rs.getString("password")+"");
                    un=rs.getString("user_name");
                    ad=rs.getString("address");
                    bir=rs.getString("birthday");
                    ph=rs.getString("phone");
                    s=rs.getString("sex");
                }
                mHanler.sendEmptyMessage(0);
//                rs.close();
//                sta.close();
                con.close(); // 关闭所有已经打开的资源
            } catch (ClassNotFoundException e ) {
                e.printStackTrace();
                Log.i("LoginActivity", "连接失败:"+e.getMessage()+"");
            }
        }
    };
    /*
        保存信息
     */
    Runnable save_info = new Runnable() {
        private Connection con = null;// 获得连接对象
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
            SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
            String user_id= pref.getString("user_id","");
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                String user_name=tv_name.getText().toString().trim();
                String user_sex=tv_sex.getText().toString().trim();
                String user_birth=tv_birth.getText().toString().trim();
                String user_phone=tv_phone.getText().toString().trim();
                String user_address=tv_address.getText().toString().trim();
                Statement sta = con.createStatement(); // 创建语句对象
                int result=sta.executeUpdate("UPDATE base_user SET user_name=('"+user_name+"') ," +
                        "sex=('"+user_sex+"'),address=('"+user_address+"')," +
                        "phone=('"+user_phone+"'),birthday=('"+user_birth+"') " +
                        "WHERE user_id=('"+user_id+"')");// 执行SQL语句
                if(result>0){
                    Looper.prepare();
                    SharedPreferences.Editor edit=pref.edit();
                    edit.putString("user_name",user_name);
                    edit.apply();
                    Toast toast=Toast.makeText(User_detail.this,"修改成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Intent intent=new Intent(User_detail.this,User.class);
                    startActivity(intent);
                    finish();
                    Looper.loop();
                }
                sta.close();
                con.close(); // 关闭所有已经打开的资源
            } catch (ClassNotFoundException e ) {
                e.printStackTrace();
                Log.i("LoginActivity", "连接失败:"+e.getMessage()+"");
            }
//            finally {
//                if (con1 != null)
//                    try {
//                        con1.close();
//                        Log.i("LoginActivity", "连接关闭:");
//                    } catch (SQLException sqle) {
//                        sqle.printStackTrace();
//                        Log.i("LoginActivity", "连接失败:"+sqle.getMessage()+"");
//                    }
//            }
        }
    };

    @Override
    public void onClick(View view) {

    }
};
