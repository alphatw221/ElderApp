package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elderApp.ElderApp.R;

public class PassportActivity extends AppCompatActivity {


    private Button exitButton;
    private TextView eventNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);

        exitButton = findViewById(R.id.exitButton);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        exitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String eventName = getIntent().getStringExtra("name");
        if(eventName.isEmpty()){
            finish();
            return;
        }
        eventNameTextView.setText(eventName);

    }

}