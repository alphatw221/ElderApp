package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Model_Class.User;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Button login,signup;
    private TextView account,password;
    private SharedPreferences preference;
    private Context context;
    private User user;
    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        login=(Button)findViewById(R.id.button1);
        signup=(Button) findViewById(R.id.button2);
        account=(TextView)findViewById(R.id.editText1);
        password=(TextView)findViewById(R.id.editText2);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getApplicationContext();
        user=new User();
        login.setOnClickListener(login_listener);
        signup.setOnClickListener(signup_listener);
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
    }
    //-------------------登入按鈕Listener---------------------------------------------------------------------------------------------------------------------
    private  Button.OnClickListener login_listener =new Button.OnClickListener(){     //登入按鈕
        @Override
        public void onClick(View v) {
            Object[] key=new Object[]{"email","password"};
            Object[] value=new Object[]{account.getText().toString(),password.getText().toString()};
            String url = "https://www.happybi.com.tw/api/auth/login";

            //---------------------執行請求------------------------------------------------------------
//            new myJsonRequest(url,"post",key,value,context,RL,REL).Fire();
            myJsonRequest.POST_Request.getJSON_object(url,key,value,context,RL,REL);
        }
    };
    //-----------------註冊按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private  Button.OnClickListener signup_listener =new Button.OnClickListener(){      //註冊按鈕


        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    };
    //---------------------回報Listener------------------------------------------------------------
    private Response.Listener<JSONObject> RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            if(response.has("access_token")){
                try{
                    preference.edit().putString("access_token",response.getString("access_token")).commit();
                    user.access_token=response.getString("access_token");
                    user.user_id=response.getInt("user_id");
                    user.id_code=response.getString("id_code");
                    user.email=response.getString("email");
                    user.name=response.getString("name");
                    user.wallet=response.getInt("wallet");
                    user.rank=response.getInt("rank");
                    user.org_rank=response.getInt("org_rank");



                }catch (JSONException e){

                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("User",user);
                startActivity(intent);


                finish();

            }

        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
//                    .setMessage("登入失敗")
                    .setMessage(error.toString())
                    .show();
        }
    };
}
//----------------------------------------------------------------------------------------------------------------------------------------

