package com.elderApp.ElderApp.Activity;

import androidx.annotation.NonNull;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Button login, signup;
    private TextView account, password;
    private SharedPreferences preference;
    public Context context;
    private User user;
    private int versionCode;
    private String update_url;


    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        login = (Button) findViewById(R.id.button1);
        signup = (Button) findViewById(R.id.button2);
        account = (TextView) findViewById(R.id.editText1);
        password = (TextView) findViewById(R.id.editText2);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------

        context = this;
        user = new User();
        login.setOnClickListener(btn_listener);
        signup.setOnClickListener(btn_listener);
        preference = getSharedPreferences("preFile", MODE_PRIVATE);
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //-------------------按鈕Listener---------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener btn_listener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    //login_request();
                    loginRequest();
                    break;
                case R.id.button2:
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };
    //---------------------回報Listener------------------------------------------------------------

    private void loginRequest(){

        final String _email = account.getText().toString();
        final String _password = password.getText().toString();

        apiService.loginRequest(this.context, _email, _password, this.versionCode, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (response.has("android_update_url")) {
                    try {
                        String androidUpdateUrl =  response.getString("android_update_url");
                        askToUpdate(androidUpdateUrl);
                    } catch (JSONException e) {
                    }
                    return;
                }

                if (response.has("access_token")) {

                    try {
                        preference.edit()
                                .putString("access_token", response.getString("access_token"))
                                .putString("email",_email)
                                .putString("password",_password)
                                .commit();

                        user = User.getInstance(response);

                        uploadPushToken();
                        navigateToIndexPage();
                    }catch(JSONException e){
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                if (error.networkResponse != null) {
                    new ios_style_alert_dialog_1.Builder(context).setTitle("登入失敗").setMessage("帳號或密碼錯誤").show();
                    return;
                }
                new ios_style_alert_dialog_1.Builder(context).setTitle("登入失敗").setMessage("請檢查網路連線").show();
            }
        });
    }


    /**
     * 詢問是否更新版本
     * @param updateUrl
     */
    private void askToUpdate(final String updateUrl){

        new ios_style_alert_dialog_1.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(updateUrl));
                context.startActivity(i);
            }
        }).setNegativeButton("否", null).show();

    }

    /**
     * 導向到首頁
     */
    private void navigateToIndexPage(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }


    private void uploadPushToken(){
        System.out.println("uploadPushToken !!!");
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    System.out.println("getInstanceId failed");
                    return;
                }

                String pushtoken = task.getResult().getToken();
                System.out.println(pushtoken);
                apiService.uploadPushTokenRequest(context,pushtoken,new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                });
            }
        });
    }



}


