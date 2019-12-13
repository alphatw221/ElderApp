package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Button login,signup;
    private TextView account,password;
    private SharedPreferences preference;
    private Context context;
    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------檢查token----------------------------------------------------------------------------------------------------------------------------
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        try{
            if(preference.contains("access_token")){
                Intent intent = new Intent();
//                intent.setClass(MainActivity.this,MainPageActivity.class);
                intent.setClass(MainActivity.this,TabActivity.class);

                startActivity(intent);
            }
        }catch (Exception e){

        }

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        login=(Button)findViewById(R.id.button1);
        signup=(Button) findViewById(R.id.button2);
        account=(TextView)findViewById(R.id.editText1);
        password=(TextView)findViewById(R.id.editText2);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getApplicationContext();
        login.setOnClickListener(login_listener);
        signup.setOnClickListener(signup_listener);
    }
//-------------------登入按鈕Listener---------------------------------------------------------------------------------------------------------------------
    private  Button.OnClickListener login_listener =new Button.OnClickListener(){     //登入按鈕
        @Override
        public void onClick(View v) {
            Object[] key=new Object[]{"email","password"};
            Object[] value=new Object[]{account.getText().toString(),password.getText().toString()};
            String url = "https://www.happybi.com.tw/api/auth/login";
            //---------------------回報Listener------------------------------------------------------------
            Response.Listener<JSONObject> RL=new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("access_token")){
                        try{
                            preference.edit().putString("access_token",response.getString("access_token")).commit();
                        }catch (JSONException e){

                        }
                        Intent intent = new Intent();
//                        intent.setClass(MainActivity.this,MainPageActivity.class);
                        intent.setClass(MainActivity.this,TabActivity.class);
                        startActivity(intent);

                    }

                }
            };
            //---------------------錯誤回報Listener------------------------------------------------------------
            Response.ErrorListener REL=new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("錯誤")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(error.toString())
                            .show();
                }
            };
            //---------------------執行請求------------------------------------------------------------
            new myJsonObjectRequest(url,"post",key,value,context,RL,REL).Fire();
        }
    };
//-----------------註冊按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private  Button.OnClickListener signup_listener =new Button.OnClickListener(){      //註冊按鈕


        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,RegistrationActivity.class);
            startActivity(intent);
        }
    };
}
//----------------------------------------------------------------------------------------------------------------------------------------
