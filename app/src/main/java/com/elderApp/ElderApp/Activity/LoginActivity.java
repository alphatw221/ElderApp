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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;

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
    private String pushtoken;

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
        pushtoken = getSharedPreferences("preFile", MODE_PRIVATE).getString("pushtoken", "");
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
                        preference.edit().putString("access_token", response.getString("access_token")).commit();
                        preference.edit().putString("email",_email).commit();
                        preference.edit().putString("password",_password).commit();

                        user.access_token = response.getString("access_token");
                        user.user_id = response.getInt("user_id");
                        user.id_code = response.getString("id_code");
                        user.email = response.getString("email");
                        user.name = response.getString("name");
                        user.wallet = response.getInt("wallet");
                        user.rank = response.getInt("rank");
                    }catch(JSONException e){
                    }
                    upload_push_token();
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


//    private void login_request() {
//        String url = "https://www.happybi.com.tw/api/auth/login";
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("email", account.getText().toString());
//            jsonObject.put("password", password.getText().toString());
//            jsonObject.put("androidVer", versionCode);
//        } catch (JSONException e) {
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jsonObject,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.has("access_token")) {
//                                preference.edit().putString("access_token", response.getString("access_token")).commit();
//                                preference.edit().putString("email",account.getText().toString()).commit();
//                                preference.edit().putString("password",password.getText().toString()).commit();
//                                user.access_token = response.getString("access_token");
//                                user.user_id = response.getInt("user_id");
//                                user.id_code = response.getString("id_code");
//                                user.email = response.getString("email");
//                                user.name = response.getString("name");
//                                user.wallet = response.getInt("wallet");
//                                user.rank = response.getInt("rank");
////                                user.org_rank=response.getInt("org_rank");
//                                upload_push_token();
//                            } else if (response.has("android_update_url")) {
//                                update_url = response.getString("android_update_url");
//                                new ios_style_alert_dialog_1.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent i = new Intent(Intent.ACTION_VIEW);
//                                        i.setData(Uri.parse(update_url));
//                                        context.startActivity(i);
//                                    }
//                                }).setNegativeButton("否", null).show();
//                            }
//                        } catch (JSONException e) {
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error.networkResponse != null) {
//                    new ios_style_alert_dialog_1.Builder(context)
//                            .setTitle("登入失敗")
//                            .setMessage("帳號或密碼錯誤")
//                            .show();
//                } else {
//                    new ios_style_alert_dialog_1.Builder(context)
//                            .setTitle("登入失敗")
//                            .setMessage("請檢查網路連線")
//                            .show();
//                }
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
//    }

    private void upload_push_token() {
        String url2 = "https://www.happybi.com.tw/api/auth/set_pushtoken";

        StringRequest stringRequest = new StringRequest(1, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context).setMessage("連線錯誤").show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String Token = getSharedPreferences("preFile", MODE_PRIVATE).getString("access_token", "");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + Token);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("pushtoken", pushtoken);
                } catch (JSONException e) {

                }
                return jsonObject.toString().getBytes();
            }

            ;
        };
        MySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }
}


