package com.example.wang.myandroidproject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
 * Created by wang on 2018/5/6.
 */

public class SplashActivity extends AppCompatActivity{
    private SharedPreferences pref;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashactivity);
        pref=getSharedPreferences("data",MODE_PRIVATE);
        user_id=pref.getString("user_id","");
        if(user_id.equals("")){
            handler.sendEmptyMessageDelayed(1,1000*3);
        }else{
            handler.sendEmptyMessageDelayed(2,1000*3);
        }

    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case 1:
                    Intent intent=new Intent(SplashActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                case 2:
                    intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                default:
                    break;
            }
        }
    };

}
