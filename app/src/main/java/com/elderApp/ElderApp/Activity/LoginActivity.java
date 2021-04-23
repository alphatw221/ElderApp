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
import android.widget.ProgressBar;
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
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Button login, signup,lineLoginButton;
    private TextView account, password;
    private SharedPreferences preference;
    public Context context;
    private User user;
    private int versionCode;
    private String update_url;
    private ProgressBar spinner;

    private static final int LINE_REQUEST_CODE = 11;


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
        lineLoginButton = findViewById(R.id.lineLoginButton);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------

        context = this;
        user = new User();
        login.setOnClickListener(btn_listener);
        signup.setOnClickListener(btn_listener);
        lineLoginButton.setOnClickListener(lineLoginListener);

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
        spinner.setVisibility(View.VISIBLE);

        apiService.loginRequest(this.context, _email, _password, this.versionCode, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                uploadPushToken();
                MainActivity.handleLoginResponse(context,response,null,_password);
                spinner.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                spinner.setVisibility(View.GONE);
                if (error.networkResponse != null) {
                    new ios_style_alert_dialog_1.Builder(context).setTitle("登入失敗").setMessage("帳號或密碼錯誤").show();
                    return;
                }
                new ios_style_alert_dialog_1.Builder(context).setTitle("登入失敗").setMessage("請檢查網路連線").show();
            }
        });
    }

    private Button.OnClickListener lineLoginListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            startLineLogin();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LINE_REQUEST_CODE) {
            return;
        }
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {
            case SUCCESS:
                // Login successful
                String userId = result.getLineProfile().getUserId();
                lineLoginRequest(userId);
                break;
            case CANCEL:
                // Login canceled by user
                System.out.println("canceled");
                break;
            default:
                // Login canceled due to other error
                System.out.println("error");
        }

    }


    private void startLineLogin(){
        try {
            Intent loginIntent = LineLoginApi.getLoginIntent(context,MainActivity.line_channelId,new LineAuthenticationParams.Builder()
                    .scopes(Arrays.asList(Scope.PROFILE))
                    // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                    .build());
            startActivityForResult(loginIntent, LINE_REQUEST_CODE);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void lineLoginRequest(String userId){
        spinner.setVisibility(View.VISIBLE);
        apiService.lineLoginRequest(context,userId,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                uploadPushToken();
                MainActivity.handleLoginResponse(context,response,userId,null);
                spinner.setVisibility(View.GONE);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("binding error");
                spinner.setVisibility(View.GONE);
            }
        });
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


