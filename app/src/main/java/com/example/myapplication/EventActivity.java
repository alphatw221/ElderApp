package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        bottomNav=findViewById(R.id.event_bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_event);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.event_fragment_container,new Frag2()).commit();
    }
    @Override
    protected void onStart() {
        super.onStart();

        bottomNav.setSelectedItemId(R.id.nav_event);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            intent.setClass(EventActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            break;
                        case R.id.nav_event:
//                            intent.setClass(EventActivity.this,EventActivity.class);
//                            startActivity(intent);
                            break;
                        case R.id.nav_myAccount:
                            intent.setClass(EventActivity.this,myAccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            break;
                    }
                    return false;
                }
            };
}
