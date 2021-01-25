package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elderApp.ElderApp.R;

public class LocationDetail extends AppCompatActivity {


    private TextView location_name;
    private TextView location_address;
    private Button map_button;
    private Button exit_button;

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        location_name = findViewById(R.id.location_name);
        location_address = findViewById(R.id.location_address);
        map_button = findViewById(R.id.map_button);
        map_button.setOnClickListener(openMap);
        exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(exit);

        Intent intent = getIntent();
        location_name.setText("名稱:" + intent.getStringExtra("name"));
        location_address.setText("地址:" + intent.getStringExtra("address"));
        link = intent.getStringExtra("link");
    }


    private Button.OnClickListener openMap = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(i);
        }
    };

    private Button.OnClickListener exit = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}