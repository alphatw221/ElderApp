package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPageActivity extends AppCompatActivity {
    //-------------------宣告全域變數----------------------------------------------------------------------------------------------------------------------------------
    private SharedPreferences preference;

    Button logout_button;
    TextView name,wallet;


    //-----------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //-------------------開始抓取物件----------------------------------------------------------------------------------------------------------------------------------
        name=(TextView)findViewById(R.id.textView);
        wallet=(TextView)findViewById(R.id.textView2);
        logout_button=(Button)findViewById(R.id._logout_button);
        //--------------------初始設定---------------------------------------------------------------------------------------------------------------------------------
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        logout_button.setOnClickListener(logout_Listener);

    }

    //-----------------------------登出按鈕Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener logout_Listener=new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            preference.edit().remove("access_token").commit();
            Intent intent = new Intent();
            intent.setClass(MainPageActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
    //-----------------------------鎖住返回鍵------------------------------------------------------------------------------------------------------------------------
    @Override
    public  void onBackPressed(){}

}
