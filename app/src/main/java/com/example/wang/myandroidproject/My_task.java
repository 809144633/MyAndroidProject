package com.example.wang.myandroidproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2018/5/27.
 */

public class My_task extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    //    private String[] title={"有没有人帮忙带饭？","有一道高数题求助！！！","快递快递急急急"};
//    private String[] short_des={"送到二号楼505","价格仅供参考，面议","再晚就要关门啦！！！"};
//    private String[] sort={"跑腿","跑腿","教学"};
//    private String[] price={"2","10","2"};
    private SimpleAdapter sim_adapter;
    private List<Map<String,Object>> datalist;
    private ListView list_view;
    private Button btn_personal;
    private Button btn_message;
    private TextView tv_title_name;
    private Button cancle;
    private Button btn_index;
    private ImageButton btn_add;
    private String array[] = new String[]{"校园任务", "二手商品"};
    SharedPreferences pref;
    private String send_info;
    private String user_id;
    private String map_value="";
    @SuppressLint("HandlerLeak")
    private Handler mHanler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            sim_adapter = new SimpleAdapter(My_task.this,datalist, R.layout.my_task_listview,
                    new String[]{"title", "short_des","sort","price","invisible_id"},
                    new int[]{R.id.title, R.id.short_description,R.id.sort,R.id.price,R.id.invisible_id});
            list_view.setAdapter(sim_adapter);
            if(map_value.equals("")&&send_info.equals("2")){
                Toast toast=Toast.makeText(My_task.this,"你目前还没有发布任务，点击下面添加按钮进行发布吧！", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else if(map_value.equals("")&&send_info.equals("1")){
                Toast toast=Toast.makeText(My_task.this,"你目前还没有接单，请前往首页寻找任务吧！", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_task);
        btn_personal=findViewById(R.id.personal);
        list_view=findViewById(R.id.listview_for_task);
        tv_title_name=findViewById(R.id.title_name);
        cancle=findViewById(R.id.cancle);
        btn_index=findViewById(R.id.btn_index);
        btn_add=findViewById(R.id.btn_add);
        pref=getSharedPreferences("data",MODE_PRIVATE);
        send_info=pref.getString("send_info","");
        user_id=pref.getString("user_id","");

        if(send_info.equals("2")){
            tv_title_name.setText("我发布的");
            datalist=new ArrayList<>();
            new Thread(runnable).start();
        }
        else if(send_info.equals("1")){
            tv_title_name.setText("我的接单");
            datalist=new ArrayList<>();
            new Thread(runnable).start();
        }

        /*
            跳转详情
         */
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map;
                map = (HashMap<String,Object>)parent.getItemAtPosition(position);
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("invisible_id", map.get("invisible_id").toString());
                editor.apply();
                Intent intent = new Intent(My_task.this, task_details.class);
                startActivity(intent);
            }
        });

        /*
            界面关闭
         */
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                My_task.this.finish();
            }
        });
        /*
            底部导航栏监听
         */
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(My_task.this,User.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                My_task.this.finish();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad =new AlertDialog.Builder(My_task.this).setTitle("请选择")
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListView lw = ((AlertDialog) dialogInterface).getListView();
                                Object checkedItem = lw.getAdapter().getItem(i);
                                dialogInterface.dismiss();
                                String xz=(String) checkedItem;
                                /*toast.makeText(MainActivity.this,xz,Toast.LENGTH_SHORT).show();*/  //测试xz的值。
                                if (xz.equals("校园任务")){
                                    Intent intent = new Intent(My_task.this, Publish_task.class);
                                    startActivity(intent);}
                                else{
                                    Intent intent = new Intent(My_task.this, Publish_product.class);
                                    startActivity(intent);
                                }
                            }

                        });
                AlertDialog dialog = ad.create();
                dialog.show();
            }
        });
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(My_task.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                My_task.this.finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        int str = position;
//        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("position", String.valueOf(str));
//        editor.apply();
//        Intent intent = new Intent(My_task.this,task_details.class);
//        startActivity(intent);
//        Log.i("catch","sdfgdg");

    }
    Runnable runnable = new Runnable() {
        private Connection con = null;// 获得连接对象
        private ResultSet rs;
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
                if(send_info.equals("2")){
                    rs = sta.executeQuery("SELECT * FROM task WHERE user_id=('"+user_id+"') and destroy_time is NULL ORDER BY create_time DESC");// 执行SQL语句
                }else {
                    rs = sta.executeQuery("SELECT task.* FROM user_task,task WHERE '"+user_id+"'=user_task.user_id and user_task.task_id=task.id and task.flag=1 and task.destroy_time is NULL ORDER BY create_time DESC");// 执行SQL语句
                }
                while(rs.next()){
//                    task_messgae task=new task_messgae();
//                    task.setItemtitle(rs.getString("title"));
//                    task.setItemContent(rs.getString("content"));
//                    task.setSort(rs.getString("sort"));
//                    double setprice=Double.parseDouble(rs.getString("price"));
//                    task.setPrice(setprice);
//                    String Itemtitle=task.getItemtitle();
//                    String ItemContent=task.getItemContent();
//                    String Sort=task.getSort();
//                    double price=task.getPrice();
                    Map<String, Object>map = new HashMap<String, Object>();
                    map.put("title",rs.getString("title"));
                    map.put("short_des",rs.getString("content"));
                    map.put("sort",rs.getString("sort"));
                    map.put("price",rs.getString("price"));
                    map.put("invisible_id",rs.getInt("id"));
                    datalist.add(map);
                    map_value=map.get("title").toString();
                    Log.i("title",map_value);
                }
            } catch (ClassNotFoundException e ) {
                e.printStackTrace();
                Log.i("LoginActivity", "连接失败:"+e.getMessage()+"");
            }
//            rs.close();
//            sta.close();
            mHanler.sendEmptyMessage(0);
            con.close(); // 关闭所有已经打开的资源
        }
    };
}
