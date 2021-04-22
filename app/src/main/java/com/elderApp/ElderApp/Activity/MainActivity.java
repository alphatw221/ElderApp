package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.AlertHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private SharedPreferences preference;
    private User user=new User();
    private Context context;
    public static Context sharedContext;
    private int versionCode;
    public static String line_channelId = "1655872039";
    public static String actionUrl = null;

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MainActivity.sharedContext = getApplicationContext();
        Intent intent = getIntent();
        if(intent.hasExtra("actionUrl")){
            MainActivity.actionUrl = intent.getStringExtra("actionUrl");
        }

        setContentView(R.layout.activity_main);
        preference = getSharedPreferences("preFile", MODE_PRIVATE);

        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("MainActivity onResume");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preference.contains("line_id")){
                    lineLoginRequest();
                } else if(preference.contains("email") && preference.contains("password")){
                    login_request();
                }else{
                    MainActivity.navigate_loginActivity(context);
                }
            }
        }, 500); // wait for 0.5 seconds

    }

    private static void handleFcmMessage(){
        System.out.println("handleActionUrl");
        if(MainActivity.actionUrl != null){
            System.out.println("ActionUrl GET");
            Intent intent = new Intent("notification-action");
            intent.putExtra("actionUrl",MainActivity.actionUrl);
            LocalBroadcastManager.getInstance(MainActivity.sharedContext).sendBroadcast(intent);
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

                MainActivity.handleLoginResponse(context,response,null,_password);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.navigate_loginActivity(MainActivity.sharedContext);
            }
        });

    }


    private void lineLoginRequest(){
        final String line_id = preference.getString("line_id","");

        apiService.lineLoginRequest(context,line_id,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                MainActivity.handleLoginResponse(context,response,line_id,null);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("binding error");
                AlertHandler.alert(context, "訊息", "登入失敗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
            }
        });

    }

    public static SharedPreferences getSharedPreferences (Context ctxt) {
        return ctxt.getSharedPreferences("preFile", MODE_PRIVATE);
    }


    public static void handleLoginResponse(Context context,JSONObject response,String line_id,String password){
        if (response.has("access_token")) {

            SharedPreferences preference = MainActivity.getSharedPreferences(MainActivity.sharedContext);

            preference.edit()
                    .putString("access_token", response.optString("access_token"))
                    .putString("email", response.optString("email")).commit();

            if(line_id != null){
                preference.edit().putString("line_id", line_id).commit();
            }else if(password != null){
                preference.edit().putString("password", password).commit();
            }

            TabActivity.user = User.getInstance(response);
            MainActivity.navigate_tabActivity(context);

        } else if (response.has("android_update_url")) {

            String update_url = response.optString("android_update_url");
            if(!update_url.isEmpty()){
                MainActivity.handle_update_url(context,update_url);
            }

        }else{
            MainActivity.navigate_loginActivity(context);
        }
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
    private static void handle_update_url(Context context,final String update_url){

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
    private static void navigate_tabActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
        Activity activity = (Activity) context;
        MainActivity.handleFcmMessage();
        activity.finish();
    }

    /**
     * 導向到 loginActivity
     */
    private static void navigate_loginActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
        Activity activity = (Activity) context;
        activity.finish();
    }

}