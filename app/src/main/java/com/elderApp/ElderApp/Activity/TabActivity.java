package com.elderApp.ElderApp.Activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.elderApp.ElderApp.Helper_Class.OrdersPagerAdapter;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class TabActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private Context context;
    private OrdersPagerAdapter ordersPagerAdapter;
    private ViewPager2 viewpager2;
    private TabLayout tabLayout;

    public static User user;

    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        user=(User)getIntent().getExtras().get("User");
        context=this.getApplicationContext();
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





}