package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

public class GiveMoneyActivity extends AppCompatActivity {

    private Context context;

    private ImageButton exitImageButton;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_give_money);

        exitImageButton = findViewById(R.id.exitImageButton);
        surfaceView = findViewById(R.id.surfaceView);
        barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(context,barcodeDetector).setRequestedPreviewSize(300,300).setAutoFocusEnabled(true).build();

        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                System.out.println("Camera Released");
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0){

                    final String text = qrCodes.valueAt(0).displayValue;
                    System.out.println(text);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.release();
                            navigate_confirmActivity(text);
                        }
                    });

                }
            }
        });

        exitImageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {

            System.out.println("Granted");
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    System.out.println("surfaceCreated");
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        System.out.println("not permited");
                        return;
                    }

                    try{
                        cameraSource.start(holder);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
        }
        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) { }
        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { }
    };

    private void navigate_confirmActivity(String text){

        String[] _text = text.split(",");
        if(_text.length != 3){
            ErrorHandler.alert(context,"錯誤","條碼錯誤");
            return;
        }

        String take_id = _text[0];
        String take_name = _text[1];
        String take_email = _text[2];

        Intent intent = new Intent(this,GiveMoneyConfirmActivity.class);
        intent.putExtra("take_id",take_id);
        intent.putExtra("take_name",take_name);
        intent.putExtra("take_email",take_email);
        startActivity(intent);
        finish();
    }


}