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
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class index extends Fragment implements OnClickListener,OnItemClickListener,OnBannerListener{
    private static String strCon = "jdbc:mysql://101.132.152.183:3306/android_server"; // 连接字符串
    private static String strUser = "root"; // 数据库用户名
    private static String strPwd = "root"; // 口令
    private Button bt1, bt2;
//    private ListView list;
    private ArrayList<Integer> list_path;
    private ArrayList<String> list_title;
    private Banner banner;
//    private ArrayList<Map<String,Object>> li;
    private List<Task> li=new ArrayList<>();
    private RecyclerView list;
        @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
        super.handleMessage(msg);
//            SimpleAdapter mAdapter= new SimpleAdapter(getActivity(),li,R.layout.main_item,new String[]
//                    {"title","content","sort","price","create_time","invisible_id"},new int[]{R.id.item_title,
//                    R.id.item_text,R.id.item_sort,R.id.item_price,R.id.item_time,R.id.invisible_id});
//            list.setAdapter(mAdapter);
            list=getActivity().findViewById(R.id.list1);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
            list.setLayoutManager(layoutManager);
            Task_Adapter adapter=new Task_Adapter(li);
            list.setAdapter(adapter);

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.index,container,false);
            bt1 = view.findViewById(R.id.btn1);
            bt2 = view.findViewById(R.id.btn2);
            banner=view.findViewById(R.id.banner);
//            list=view.findViewById(R.id.list1);
            banner=view.findViewById(R.id.banner);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
//        list.setOnItemClickListener(this);
//        li=new ArrayList<Map<String, Object>>();
        new Thread(mrunnable).start();
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(getActivity(), tasklist.class);
                startActivity(intent);
                break;

            case R.id.btn2:
                intent = new Intent(getActivity(), goods_changelist.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        HashMap<String, Object> map;
        map = (HashMap<String,Object>)parent.getItemAtPosition(position);
        SharedPreferences pref=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("invisible_id", map.get("invisible_id").toString());
        editor.apply();
        Intent intent = new Intent(getActivity(), task_details.class);
        startActivity(intent);
    }

    private void initView(){
        list_path=new ArrayList<>();
        list_title=new ArrayList<>();
        list_path.add(R.drawable.f);
        list_path.add(R.drawable.g900);
        list_path.add(R.drawable.g9002);
        list_title.add("这是我们的大学");
        list_title.add("最新款鼠标！！！");
        list_title.add("大卖特卖！！！");
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new MyLoader());
        banner.setImages(list_path);
        banner.setBannerAnimation(Transformer.Default);
        banner.setBannerTitles(list_title);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(this)
                .start();
    }
    Runnable mrunnable = new Runnable() {
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
                String sql="select * from task WHERE flag=0 and destroy_time is NULL ORDER BY create_time DESC";
                if (con!=null&&!con.isClosed()){
                    ps= (PreparedStatement) con.prepareStatement(sql);
                    if(ps!=null){
                        rs=ps.executeQuery();
                        if (rs!=null){
                            while (rs.next()){
//                                task_messgae task=new task_messgae();
//                                task.setItemtitle(rs.getString("title"));
//                                task.setItemContent(rs.getString("content"));
//                                task.setSort(rs.getString("sort"));
//                                task.setPrice(rs.getInt("price"));
//                                task.setId(rs.getInt("id"));
                               /* BigDecimal beichushu=new BigDecimal(sPrice);
                                BigDecimal chushu=new BigDecimal(100000000);*/

                               /* BigDecimal result=beichushu.divide(chushu,new MathContext(4));*/

                                /*double sPrice=Double.valueOf(rs.getString("price")) ;*/

                               String Itemtitle=rs.getString("title");
                               String ItemContent=rs.getString("content");
                               String Sort=rs.getString("sort");
                               String time=rs.getString("create_time");
                               @SuppressLint("SimpleDateFormat")
                               SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                               String real_time = sdf.format(new Date(Long.parseLong(time)));
                               int id=rs.getInt("id");
                               int Price=rs.getInt("price");
                                Task itemData=new Task(Itemtitle,ItemContent,Price,Sort,real_time,id);
                                li.add(itemData);
//                               Map<String,Object> itemData = new HashMap<String,Object>();
//                                    itemData.put("title",Itemtitle);
//                                    itemData.put("content",ItemContent);
//                                    itemData.put("sort","分类："+Sort);
//                                    itemData.put("price",Price);
//                                    itemData.put("create_time",real_time);
//                                    itemData.put("invisible_id",id);

                            }
                            mHandler.sendEmptyMessage(0);
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

            con.close(); // 关闭所有已经打开的资源
        }
    };

//    private class MyLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context).load((Integer) path).into(imageView);
//        }
//    }

    @Override
    public void OnBannerClick(int position) {
        Log.i("tag","你点击了第"+position+"张图片");
    }

    private class MyLoader implements ImageLoaderInterface {

        @Override
        public void displayImage(Context context, Object path, View imageView) {
            Glide.with(context).load((Integer) path).into((ImageView) imageView);
        }

        @Override
        public View createImageView(Context context) {
            return null;
        }
    }
}
