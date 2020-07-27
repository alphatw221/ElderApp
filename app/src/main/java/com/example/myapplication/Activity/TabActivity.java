package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Fragment.Frag1;
import com.example.myapplication.Fragment.Frag2;
import com.example.myapplication.Fragment.Frag3;
import com.example.myapplication.Helper_Class.OrdersPagerAdapter;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.MenuItem;
import android.widget.TableLayout;

import org.json.JSONObject;

public class TabActivity extends AppCompatActivity implements Frag2.change_frag2{
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Context context;
    private OrdersPagerAdapter ordersPagerAdapter;
    private Frag2.change_frag2 c=this;
    private ViewPager2 viewpager2;
    private TabLayout tabLayout;
    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        //---------------------發出請求------------------------------------------------------------
        //-----------------初始設定----------------------------------------------------------------
        context=this.getApplicationContext();
        viewpager2 = findViewById(R.id._viewpager2);

        ordersPagerAdapter=new OrdersPagerAdapter(this,c);
        viewpager2.setAdapter(ordersPagerAdapter);

        tabLayout = findViewById(R.id._tab_layout);

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
    public  void onBackPressed(){
//        getSharedPreferences("preFile",MODE_PRIVATE).edit().remove("access_token").commit();
    }


    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(TabActivity.this)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("連線逾時 請從新登入")
                    .show();
            getSharedPreferences("preFile",MODE_PRIVATE).edit().remove("access_token").commit();

            finish();
            //
        }
    };

    @Override
    public void Change_frag2(Event_class event_class) {
        ordersPagerAdapter.change_frag2(event_class);
        viewpager2.setAdapter(ordersPagerAdapter);
        viewpager2.setCurrentItem(1);
    }
}