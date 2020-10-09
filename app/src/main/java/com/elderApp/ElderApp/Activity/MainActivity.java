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
import android.os.Handler;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
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
    private String pushtoken;

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_main);
        preference = getSharedPreferences("preFile", MODE_PRIVATE);
        pushtoken = preference.getString("pushtoken", "");
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                //------------檢查版本----------------------------------------------------------------------------------------------------------------------------
                //------------執行通知設定----------------------------------------------------------------------------------------------------------------------------
                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener() {
//                            @Override
//                            public void onComplete(@NonNull Task task) {
//                                if (!task.isSuccessful()) {
//                                    return;
//                                }
//                                if( task.getResult() == null)
//                                    return;
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//                                // Log and toast
//                                Log.i("MainActivity","token "+token);
//                            }
//                        })
                ;


//                Intent Not_intent = new Intent(this,AlertDetails.class);
//                Not_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, Not_intent, 0);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.coin)
//                        .setContentTitle("testTitle")
//                        .setContentText("testContent")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                        .setCategory(NotificationCompat.CATEGORY_EVENT)
//                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true)
//                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setAutoCancel(true);
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                // notificationId is a unique int for each notification that you must define
//                notificationManager.notify(notificationId, builder.build());
                //------------檢查token----------------------------------------------------------------------------------------------------------------------------
                try {
                    if (preference.contains("email") && preference.contains("password")) {
                        login_request();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) { }
            }
        }, 2 * 1000); // wait for 2 seconds

    }
    //------------通知頻道函式(26+)----------------------------------------------------------------------------------------------------------------------------
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "testname";
//            String description = "testdescripton";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
    private void login_request() {

        String url = "https://www.happybi.com.tw/api/auth/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", getSharedPreferences("preFile", MODE_PRIVATE).getString("email", ""));
            jsonObject.put("password", getSharedPreferences("preFile", MODE_PRIVATE).getString("password", ""));
            jsonObject.put("androidVer", versionCode);
        } catch (JSONException e) {
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("access_token")) {
                                preference.edit().putString("access_token", response.getString("access_token")).commit();
                                user.access_token = response.getString("access_token");
                                user.user_id = response.getInt("user_id");
                                user.id_code = response.getString("id_code");
                                user.email = response.getString("email");
                                user.name = response.getString("name");
                                user.wallet = response.getInt("wallet");
                                user.rank = response.getInt("rank");
//                                user.org_rank=response.getInt("org_rank");
//                                upload_push_token();
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("User", user);
                                startActivity(intent);
                                finish();
                            } else if (response.has("android_update_url")) {
                                final String update_url = response.getString("android_update_url");
                                new ios_style_alert_dialog_1.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(update_url));
                                        context.startActivity(i);
                                    }
                                }).setNegativeButton("否", null).show();
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    new ios_style_alert_dialog_1.Builder(context)
                            .setTitle("登入失敗")
                            .setMessage("帳號或密碼錯誤")
                            .show();
                } else {
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }

//    private void upload_push_token() {
//        String url2 = "https://www.happybi.com.tw/api/auth/set_pushtoken";
//
//        StringRequest stringRequest = new StringRequest(1, url2, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra("User", user);
//                startActivity(intent);
//                finish();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                new ios_style_alert_dialog_1.Builder(context).setMessage("連線錯誤").show();
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                String Token = getSharedPreferences("preFile", MODE_PRIVATE).getString("access_token", "");
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + Token);
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("pushtoken", pushtoken);
//                } catch (JSONException e) {
//
//                }
//                return jsonObject.toString().getBytes();
//            }
//
//            ;
//        };
//        MySingleton.getInstance(context).getRequestQueue().add(stringRequest);
//    }
}