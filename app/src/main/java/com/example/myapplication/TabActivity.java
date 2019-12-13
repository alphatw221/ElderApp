package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class TabActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    EditText person_name;

    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        //---------------------發出請求------------------------------------------------------------
        String url="https://www.happybi.com.tw/api/auth/me";
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
        new myJsonObjectRequest(url,"post",key,value,this.getApplicationContext(),RL,REL).Fire();
        //---------------------------------------------------------------------------------
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


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
                    .setMessage(error.toString())
                    .show();
            getSharedPreferences("preFile",MODE_PRIVATE).edit().remove("access_token").commit();
            Intent intent = new Intent();
            intent.setClass(TabActivity.this,MainActivity.class);
            startActivity(intent);
            //
        }
    };
}