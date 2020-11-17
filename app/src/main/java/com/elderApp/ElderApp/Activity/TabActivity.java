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
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("首頁");
                        tab.setIcon(R.drawable.ic_baseline_house_24);
                        break;
                    case 1:
                        tab.setText("活動");
                        tab.setIcon(R.drawable.ic_school_black_24dp);
                        break;
                    case 2:
                        tab.setText("動態");
                        tab.setIcon(R.drawable.ic_baseline_art_track_24);
                        break;
                    case 3:
                        tab.setText("我的帳戶");
                        tab.setIcon(R.drawable.ic_folder_shared_black_24dp);
                        break;
                }
            }
        });

        tabLayoutMediator.attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("TabActivity onResume !!!");

        check_isTokenValid();
    }


    private void check_isTokenValid(){
        apiService.getMeRequest(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response.optString("name"));
                System.out.println(response.optInt("wallet"));

                TabActivity.user.wallet = response.optInt("wallet");
                TabActivity.user.name = response.optString("name");
                TabActivity.user.email = response.optString("email");
                TabActivity.user.org_rank = response.optInt("org_rank");

                Intent intent = new Intent("update-user-info");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                navigate_mainActivity();
            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void navigate_mainActivity(){
        Intent intent = new Intent();
        intent.setClass(TabActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}