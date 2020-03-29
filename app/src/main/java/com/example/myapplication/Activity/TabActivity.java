package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Fragment.Frag1;
import com.example.myapplication.Fragment.Frag2;
import com.example.myapplication.Fragment.Frag3;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import org.json.JSONObject;

public class TabActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    BottomNavigationView bottomNav;

    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        //---------------------發出請求------------------------------------------------------------
        String url="https://www.happybi.com.tw/api/auth/me";
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,this.getApplicationContext(),RL,REL).Fire();
        myJsonRequest.POST_Request.getJSON_object(url,key,value,this.getApplicationContext(),RL,REL);
        //-----------------初始設定----------------------------------------------------------------

        bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Frag1()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment=new Frag1();
                            break;
                        case R.id.nav_event:
                            selectedFragment=new Frag2();
                            break;
                        case R.id.nav_myAccount:
                            selectedFragment=new Frag3();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };



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
}