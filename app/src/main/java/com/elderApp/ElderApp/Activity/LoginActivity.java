package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Button login,signup;
    private TextView account,password;
    private SharedPreferences preference;
    public Context context;
    private User user;
    private int versionCode;
    private String update_url;
    private String pushtoken;
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
        pushtoken=getSharedPreferences("preFile",MODE_PRIVATE).getString("pushtoken","");
        context=this.getApplicationContext();
        user=new User();
        login.setOnClickListener(login_listener);
        signup.setOnClickListener(signup_listener);
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    //-------------------登入按鈕Listener---------------------------------------------------------------------------------------------------------------------
    private  Button.OnClickListener login_listener =new Button.OnClickListener(){     //登入按鈕







        @Override
        public void onClick(View v) {
            Object[] key=new Object[]{"email","password","androidVer"};
            Object[] value=new Object[]{account.getText().toString(),password.getText().toString(),versionCode};
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
        public void onResponse(final JSONObject response) {
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

                //-----------------------登入成功 開始上傳pushtoken---------------------------------------------
                String url2="https://www.happybi.com.tw/api/auth/set_pushtoken";




                StringRequest stringRequest=new StringRequest(1, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String string = response.toString();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("User", user);
                        startActivity(intent);

                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(getApplicationContext()).setMessage("連線錯誤").show();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        String Token=getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+Token);
                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("pushtoken",pushtoken);
                        }catch (JSONException e){

                        }
                        return jsonObject.toString().getBytes();
                    };

//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("pushtoken",pushtoken);
//                        return params;
//                    }
                };

                MySingleton.getInstance(context).getRequestQueue().add(stringRequest);
                //--------------------------------------------------------------------------------------------



            }else if(response.has("android_update_url")){
                try {
                    update_url=response.getString("android_update_url");
                    new AlertDialog.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i =new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(update_url));
                            context.startActivity(i);
                        }
                    }).setNegativeButton("否",null).show();
                }catch (JSONException e){

                }

            }else{
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("登入失敗")
                        .setMessage("帳號或密碼錯誤")
                        .show();
            }

        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {

                if(error.networkResponse!=null){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登入失敗")
                            .setMessage("帳號或密碼錯誤")
                            .show();
                }else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登入失敗")
                            .setMessage("請檢查網路連線")
                            .show();
                }



        }
    };
}
//----------------------------------------------------------------------------------------------------------------------------------------

