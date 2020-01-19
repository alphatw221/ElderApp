package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class myAccountActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        bottomNav=findViewById(R.id.myAccount_bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_myAccount);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.myAccount_fragment_container,new Frag3()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bottomNav.setSelectedItemId(R.id.nav_myAccount);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            intent.setClass(myAccountActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            break;
                        case R.id.nav_event:
                            intent.setClass(myAccountActivity.this,EventActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            break;
                        case R.id.nav_myAccount:
//                            intent.setClass(myAccountActivity.this,myAccountActivity.class);
//                            startActivity(intent);
                            break;
                    }
                    return false;
                }
            };
}
