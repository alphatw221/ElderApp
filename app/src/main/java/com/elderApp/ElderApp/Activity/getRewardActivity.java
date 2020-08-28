package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class getRewardActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ImageButton getreward_back;
    private ZXingScannerView getreward_qrscanner;
    private ConstraintLayout getrewqrd_qrscanner_layout;
    private String url="https://www.happybi.com.tw/api/drawEventRewardV2/";
    private String slug,name;
    private Context context=this;
    private Activity activity=this;
    private ZXingScannerView.ResultHandler resultHandler=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reward);


        getreward_back=(ImageButton)findViewById(R.id._getreward_back);
        getreward_qrscanner=findViewById(R.id._getreward_qrscanner);

        getreward_back.setOnClickListener(btn_listener);
        Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();

    }

    @Override
    public void handleResult(Result rawResult) {
        String[] result=rawResult.getText().split(",");
        if(result[0].equals("reward")){
            JsonObjectRequest signinRequest = new JsonObjectRequest(1, url + result[1], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getInt("s")==1){
                            new AlertDialog.Builder(context).setMessage(response.getString("m")).show();
                            finish();
                        }else{
                            new AlertDialog.Builder(context).setMessage(response.getString("m")).show();
//                            getreward_qrscanner.stopCamera();
//                            getreward_qrscanner.startCamera();
                            Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
                        }
                    }catch (JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(context).setMessage("連線錯誤").show();
//                    getreward_qrscanner.stopCamera();
//                    getreward_qrscanner.startCamera();
                    Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String Token = getSharedPreferences("preFile", MODE_PRIVATE).getString("access_token", "");
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + Token);
                    return headers;
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(signinRequest);
        }else{
            new AlertDialog.Builder(context).setMessage("請使用對應的QR碼").show();
//            getreward_qrscanner.stopCamera();
//            getreward_qrscanner.startCamera();
            Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
        }
    }

    private PermissionListener permissionListener = new PermissionListener(){

        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
            getreward_qrscanner.setResultHandler(resultHandler);
            getreward_qrscanner.startCamera();
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {
            new AlertDialog.Builder(context).setMessage("請設定允許存取相機以領取獎勵").show();
            finish();
        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

        }
    };


    private Button.OnClickListener btn_listener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };
}