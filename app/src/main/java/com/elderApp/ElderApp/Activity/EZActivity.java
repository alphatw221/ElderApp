package com.elderApp.ElderApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class EZActivity extends AppCompatActivity {
    static User user;
    private LocationRequest locationRequest;
    private double latitude;
    private double Longitude;
    private ImageButton product1,product2,product3,event1,event2,event3,sign_in_btn,pro_btn;
    private TextView EZ_name,EZ_happybi,EZ_rank;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezactivity);

        product1=findViewById(R.id._product1);
        product2=findViewById(R.id._product2);
        product3=findViewById(R.id._product3);
        event1=findViewById(R.id._event1);
        event2=findViewById(R.id._event2);
        event3=findViewById(R.id._event3);
        sign_in_btn=findViewById(R.id._sign_in_btn);
        pro_btn=findViewById(R.id._pro_btn);

        product1.setOnClickListener(btn_listener);
        product2.setOnClickListener(btn_listener);
        product3.setOnClickListener(btn_listener);
        event1.setOnClickListener(btn_listener);
        event2.setOnClickListener(btn_listener);
        event3.setOnClickListener(btn_listener);
        sign_in_btn.setOnClickListener(btn_listener);
        pro_btn.setOnClickListener(btn_listener);

        EZ_name=findViewById(R.id._EZ_name);
        EZ_happybi=findViewById(R.id._EZ_happybi);
        EZ_rank=findViewById(R.id._EZ_rank);

        EZ_name.setText("姓名:"+EZActivity.user.name);
        EZ_happybi.setText("剩餘樂幣:"+EZActivity.user.wallet);
        EZ_rank.setText("榮譽等級:"+EZActivity.user.rank);
    }

    private ImageButton.OnClickListener btn_listener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id._sign_in_btn:
                    get_location();
                    break;

                case R.id._pro_btn:
                    TabActivity.user=user;
                    navigate_tabActivity(context);
                    break;
            }

        }
    };

    private void get_location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        if (locationRequest == null) {
            locationRequest = LocationRequest.create().setInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult( LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(EZActivity.this).removeLocationUpdates(this);
                if(locationResult != null && locationResult.getLocations().size()>0){
                    int index=locationResult.getLocations().size()-1;
                    latitude = locationResult.getLocations().get(index).getLatitude();
                    Longitude = locationResult.getLocations().get(index).getLongitude();
                    Toast.makeText(context,Double.toString(latitude)+Double.toString(Longitude),Toast.LENGTH_SHORT).show();
                }
            }
        }, Looper.getMainLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                get_location();
            }else{
                Toast.makeText(this,"請開啟權限",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 導向到 tabActivity
     */
    private void navigate_tabActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_isTokenValid();
    }

    private void check_isTokenValid(){
        apiService.getMeRequest(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                EZActivity.user.wallet = response.optInt("wallet");
                EZActivity.user.name = response.optString("name");
                EZActivity.user.email = response.optString("email");
                EZActivity.user.org_rank = response.optInt("org_rank");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                navigate_mainActivity();
            }
        });
    }

    private void navigate_mainActivity(){
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}