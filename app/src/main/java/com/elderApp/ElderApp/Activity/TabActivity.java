package com.elderApp.ElderApp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.OrdersPagerAdapter;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TabActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Context context;
    private OrdersPagerAdapter ordersPagerAdapter;
    private ViewPager2 viewpager2;
    private TabLayout tabLayout;
    private int versionCode;
    public static User user;
    private SharedPreferences preferences;

    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        preferences=getSharedPreferences("preFile", MODE_PRIVATE);
        user=(User)getIntent().getExtras().get("User");
        context=this;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) { }
        viewpager2 = findViewById(R.id._viewpager2);
        tabLayout = findViewById(R.id._tab_layout);

        ordersPagerAdapter=new OrdersPagerAdapter(this);
        viewpager2.setAdapter(ordersPagerAdapter);
        //---------------------發出請求------------------------------------------------------------

        //-----------------初始設定----------------------------------------------------------------
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(context, R.color.tabSelectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(context, R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(
                tabLayout, viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("首頁");
                        tab.setIcon(R.drawable.ic_baseline_house_24);
//                        BadgeDrawable badgeDrawable1=tab.getOrCreateBadge();
//                        badgeDrawable1.setBackgroundColor(ContextCompat.getColor(context,R.color.get_money_green));
//                        badgeDrawable1.setVisible(true);
//                        badgeDrawable1.setNumber(12);

                        break;
                    case 1:
                        tab.setText("活動");
                        tab.setIcon(R.drawable.ic_school_black_24dp);
//                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
//                        badgeDrawable.setVisible(true);
//                        badgeDrawable.setNumber(12);
                        break;
                    case 2:
                        tab.setText("動態");
                        tab.setIcon(R.drawable.ic_baseline_art_track_24);
//                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
//                        badgeDrawable.setVisible(true);
//                        badgeDrawable.setNumber(12);
                        break;
                    case 3:
                        tab.setText("我的帳戶");
                        tab.setIcon(R.drawable.ic_folder_shared_black_24dp);
//                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
//                        badgeDrawable.setVisible(true);
//                        badgeDrawable.setNumber(12);
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update_token();
    }

    private void update_token() {

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
                                getSharedPreferences("preFile", MODE_PRIVATE).edit().putString("access_token", response.getString("access_token")).commit();

                            } else if (response.has("android_update_url")) {
                                final String update_url = response.getString("android_update_url");
                                new ios_style_alert_dialog_1.Builder(context).setTitle("已有更新版本").setMessage("請更新後重新登入").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(update_url));
                                        context.startActivity(i);
                                        preferences.edit().remove("email").remove("password").remove("access_token").commit();
                                        finish();
                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        preferences.edit().remove("email").remove("password").remove("access_token").commit();
                                        finish();
                                    }
                                }).show();
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

    @Override
    public  void onBackPressed(){
//        getSharedPreferences("preFile",MODE_PRIVATE).edit().remove("access_token").commit();
    }

}