package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private SharedPreferences preference;
    private User user=new User();
    private Context context;
    private int versionCode;

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_main);
        preference = getSharedPreferences("preFile", MODE_PRIVATE);

        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }


        //------------檢查token---------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preference.contains("email") && preference.contains("password")){
                    login_request();
                }else{
                    navigate_loginActivity();
                }
            }
        }, 2 * 1000); // wait for 2 seconds

    }

    private void handleFcmMessage(){
        System.out.println("handleActionUrl");
        String actionUrl = getIntent().getStringExtra("actionUrl");
        if(actionUrl != null){
            System.out.println("ActionUrl GET");
            Intent intent = new Intent("notification-action");
            intent.putExtra("actionUrl",actionUrl);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }


    /**
     * 登入請求
     */
    private void login_request() {

        final String _email = preference.getString("email","");
        final String _password = preference.getString("password","");

        apiService.loginRequest(this.context,_email,_password,this.versionCode,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                if (response.has("access_token")) {

                    preference.edit()
                            .putString("access_token", response.optString("access_token"))
                            .putString("email", response.optString("email"))
                            .putString("password", _password)
                            .commit();

                    user = User.getInstance(response);
                    navigate_tabActivity();

                } else if (response.has("android_update_url")) {

                    String update_url = response.optString("android_update_url");
                    if(!update_url.isEmpty()){
                        handle_update_url(update_url);
                    }

                }else{
                    navigate_loginActivity();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                navigate_loginActivity();
            }
        });

    }

    /**
     * 未知錯誤訊息
     */
    private void show_unknown_error_alert(){
        new ios_style_alert_dialog_1.Builder(context)
                .setTitle("登入失敗")
                .setMessage("請檢查網路連線")
                .setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * 處理拿到 update_url 之後的動作
     * @param update_url
     */
    private void handle_update_url(final String update_url){

        new ios_style_alert_dialog_1.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(update_url));
                context.startActivity(i);
            }
        }).setNegativeButton("否", null).show();

    }


    /**
     * 導向到 tabActivity
     */
    private void navigate_tabActivity(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("User", user);
        startActivity(intent);
        handleFcmMessage();
        finish();
    }

    /**
     * 導向到 loginActivity
     */
    private void navigate_loginActivity(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}