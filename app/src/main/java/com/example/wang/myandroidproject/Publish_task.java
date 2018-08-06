package com.example.wang.myandroidproject;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/*
 * Created by wang on 2018/5/8.
 */
public class Publish_task extends AppCompatActivity {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private EditText title,content,edit_price,edit_connection,edit_address;
    private Button sort,release;
    private Toast toast;
    private String array[] = new String[]{"代拿物品", "教学服务", "其他"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_task);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        edit_price=findViewById(R.id.edit_price);
        edit_connection=findViewById(R.id.edit_connection);
        edit_address=findViewById(R.id.edit_address);
        sort = findViewById(R.id.sort);
        release=findViewById(R.id.release);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad =new AlertDialog.Builder(Publish_task.this).setTitle("请选择")
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListView lw = ((AlertDialog) dialogInterface).getListView();
                                Object checkedItem = lw.getAdapter().getItem(i);
                                dialogInterface.dismiss();
                                sort.setText((String)checkedItem);
                            }
                        });
                AlertDialog dialog = ad.create();
                dialog.show();
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
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("Publish_task", "连接失败" + e.getMessage() + "");
            }
        }
        private void sqlCon(Connection con1) throws SQLException {
            try { // 监控异常
                Class.forName("com.mysql.jdbc.Driver"); // 加载驱动程序
                Connection con;// 获得连接对象
                con = DriverManager.getConnection(strCon, strUser, strPwd);
                System.out.print("连接成功");
                Statement sta = con.createStatement(); // 创建语句对象
                String t=title.getText().toString();
                String c=content.getText().toString();
                String p=edit_price.getText().toString();
                String s=sort.getText().toString();
                String ec=edit_connection.getText().toString();
                String ea=edit_address.getText().toString();
                String create_time= String.valueOf(Calendar.getInstance().getTimeInMillis());
                SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
                String user_id= pref.getString("user_id","");

                if(t.isEmpty()||p.isEmpty()||ec.isEmpty()||s.isEmpty()){
                    Looper.prepare();
                    toast.makeText(Publish_task.this,"有内容为空，请重新输入！！！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else
                {
                    Double edit_price1=Double.parseDouble(edit_price.getText().toString());
                    result=sta.executeUpdate("INSERT INTO task(user_id,title,content,sort,price,address,phone,create_time)" +
                            " VALUES ('"+user_id+"','"+t+"','"+c+"','"+s+"','"+edit_price1+"','"+ea+"','"+ec+"','"+create_time+"')");
                }
                if(result>0){
                    Looper.prepare();
                    toast.makeText(Publish_task.this,"发布成功！",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Publish_task.this,MainActivity.class);
                    startActivity(intent);
                    Looper.loop();
                }else{
                    Log.i("Publish_task", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Publish_task", "MYSQL ERROR"+e.getMessage()+"");
            }
            con.close(); // 关闭所有已经打开的资源
        }
    };
}
