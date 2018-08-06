package com.example.wang.myandroidproject;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by wang on 2018/5/8.
 */
public class Publish_product extends AppCompatActivity {
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private EditText title,content,edit_price,edit_connection,edit_address;
    private Button sort,release;
    private Toast toast;
    private Context mcontent;
    private GridView gridview;
    private ArrayList<String> mPicList = new ArrayList<>();
//    private GridViewAdapter mGridViewAddImgAdapter;
    private String array[] = new String[]{"电子商品", "生活用品", "家居用品","其他"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_product);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        edit_price=findViewById(R.id.edit_price);
        edit_connection=findViewById(R.id.edit_connection);
        edit_address=findViewById(R.id.edit_address);
        sort = findViewById(R.id.sort);
        release=findViewById(R.id.release);
//        gridview=findViewById(R.id.GirdView);
        mcontent=this;
//        initGridView();
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder ad =new AlertDialog.Builder(Publish_product.this).setTitle("请选择")
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
//    private void initGridView(){
//        mGridViewAddImgAdapter = new GridViewAdapter(mcontent, mPicList);
//        gridview.setAdapter(mGridViewAddImgAdapter);
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
//                if (postion==parent.getChildCount()-1){
//                    /*if (mPicList.size()==MainConstant.MAX_SELECT_PIC_NUM){
//
//                    }*/
//                }
//
//            }
//        });
//
//    }

    //数据库连接及数据存放。
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
                    toast.makeText(Publish_product.this,"有内容为空，请重新输入！！！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else
                {
                    Double edit_price1=Double.parseDouble(edit_price.getText().toString());
                    result=sta.executeUpdate("INSERT INTO product(title,content,sort,address,price,phone,create_time) VALUES" +
                            " ('"+user_id+"','"+t+"','"+c+"','"+s+"','"+ea+"','"+edit_price1+"','"+ec+"','"+create_time+"')");
                }
                if(result>0){
                    Looper.prepare();
                    toast.makeText(Publish_product.this,"发布成功！",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Publish_product.this,MainActivity.class);
                    startActivity(intent);
                    Looper.loop();
                }else{
                    Log.i("Publish_product", "INSERT FAIL");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Publish_product", "MYSQL ERROR"+e.getMessage()+"");
            }
            con.close(); // 关闭所有已经打开的资源
        }
    };
    //结束。
}
