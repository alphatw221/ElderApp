package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.elderApp.ElderApp.Helper_Class.AlertHandler;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private Context context;

    private ConstraintLayout backbround;
    private Button exitButton;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    public enum ScanType {
        Arrive, Reward
    }

    ;
    private ScanType scanType;

    private String slug;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        context = this;
        scanType = (ScanType) getIntent().getSerializableExtra("scanType");

        backbround = findViewById(R.id.backbround);
        surfaceView = findViewById(R.id.surfaceView);
        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(context, barcodeDetector).setRequestedPreviewSize(300, 300).setAutoFocusEnabled(true).build();
        barcodeDetector.setProcessor(processor);
        checkCameraPermissionAndStart();

        switch (scanType) {
            case Arrive:
                backbround.setBackgroundColor(Color.parseColor("#598E9A"));
                slug = getIntent().getStringExtra("slug");
                name = getIntent().getStringExtra("name");
                checkIsArrive();
                break;
            case Reward:
                backbround.setBackgroundColor(Color.parseColor("#C15656"));
                break;
        }

    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
            System.out.println("Granted");
            startCamera();
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {
        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        }
    };

    private Detector.Processor<Barcode> processor = new Detector.Processor<Barcode>() {
        @Override
        public void release() {
            System.out.println("Camera Released");
        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
            if (qrCodes.size() != 0) {

                final String text = qrCodes.valueAt(0).displayValue;
                System.out.println(text);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cameraSource.release();
                        handleText(text);
                    }
                });

            }
        }
    };

    private void errorExit(){
        AlertHandler.alert(context, "錯誤", "條碼錯誤", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    private void handleText(String text) {
        String[] _text = text.split(",");
        if (_text.length != 2) {
            ErrorHandler.alert(context, "錯誤", "條碼錯誤");
            return;
        }

        String slug = _text[1];
        switch (_text[0]) {
            case "arrive":
                if(scanType != ScanType.Arrive){
                    errorExit();
                    break;
                }
                arriveEvent(slug);
                break;
            case "reward":
                if(scanType != ScanType.Reward){
                    errorExit();
                    break;
                }
                takeReward(slug);
                break;
            default:
                errorExit();
                break;
        }
    }

    private void checkCameraPermissionAndStart() {
        System.out.println("checkCameraPermissionAndStart");
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
    }

    private void startCamera() {
        System.out.println("startCamera");
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("surfaceCreated");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("not permited");
                    return;
                }

                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    private void takeReward(String slug) {
        apiService.takeRewardRequest(context, slug, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String title = "訊息";
                String message = "領取成功";
                if (response.optInt("s") != 1) {
                    title = "錯誤";
                    message = response.optString("m");
                }

                AlertHandler.alert(context, title, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

            }
        }, ErrorHandler.defaultListener(context));
    }


    private void checkIsArrive() {
        apiService.isUserArriveRequest(context, slug, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int s = response.optInt("s");
                if (s == 1) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.release();
                            navigate_PassportActivity(name);
                        }
                    });
                } else if (s == 2) {
                    //checkCameraPermissionAndStart();
                }else{
                    AlertHandler.alert(context, "錯誤", response.optString("m"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }
            }
        },ErrorHandler.defaultListener(context));
    }

    private void arriveEvent(String slug){
        apiService.arriveEventRequest(context, slug, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.optInt("s") != 1){
                    AlertHandler.alert(context, "錯誤", response.optString("m"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    return;
                }

                try {
                    String name = response.getString("name");
                    navigate_PassportActivity(name);
                } catch (JSONException e) {
                    return;
                }
            }
        },ErrorHandler.defaultListener(context));
    }

    private void navigate_PassportActivity(String name){
        Intent intent = new Intent(context,PassportActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
        finish();
    }



}