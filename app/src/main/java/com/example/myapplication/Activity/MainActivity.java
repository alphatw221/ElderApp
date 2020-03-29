package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Model_Class.User;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private SharedPreferences preference;
    private User user;
    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {
                //------------檢查版本----------------------------------------------------------------------------------------------------------------------------

                //------------檢查token----------------------------------------------------------------------------------------------------------------------------
                try{
                    if(preference.contains("access_token")){
                        check_token_expire();
                    }else{
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){

                }

            }

        }, 2*1000); // wait for 5 seconds



    }

    private boolean check_token_expire(){
        //---------------------發出請求------------------------------------------------------------
        String url="https://www.happybi.com.tw/api/auth/me";
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,this.getApplicationContext(),RL,REL).Fire();
        myJsonRequest.POST_Request.getJSON_object(url,key,value,this.getApplicationContext(),RL,REL);
        return true;

    };
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                user=new User();
                user.user_id=response.getInt("user_id");
                user.id_code=response.getString("id_code");
                user.email=response.getString("email");
                user.name=response.getString("name");
                user.wallet= response.getInt("wallet");
                user.rank=response.getInt("rank");
            }catch (JSONException e)
            {

            }


            Intent intent = new Intent();
            intent.setClass(MainActivity.this, myAccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("User",user);

            Intent intent1=new Intent();
            intent1.setClass(MainActivity.this, EventActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent1.putExtra("User",user);

            Intent intent2=new Intent();
            intent2.setClass(MainActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent2.putExtra("User",user);

            startActivity(intent);
            startActivity(intent1);
            startActivity(intent2);
            finish();
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            //
        }
    };
}

