package com.elderApp.ElderApp.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import com.elderApp.ElderApp.Helper_Class.QRCodeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.elderApp.ElderApp.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class TakeMoneyActivity extends AppCompatActivity {


    private ImageButton dismissButton;
    private ImageView qrcodeImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_money);
        System.out.println("TakeMoneyActivity onCreate");

        dismissButton = (ImageButton)findViewById(R.id.dismissButton);
        qrcodeImageView = (ImageView)findViewById(R.id.qrcodeImageView);


        generateQrCode();
        dismissButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("on click dismiss");
                finish();
            }
        });




    }

    private void generateQrCode(){
        String text = TabActivity.user.user_id + "," + TabActivity.user.name + "," + TabActivity.user.email;
        Bitmap bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(text)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();

        qrcodeImageView.setImageBitmap(bitmap);
        qrcodeImageView.setBackgroundColor(Color.WHITE);

    }






}