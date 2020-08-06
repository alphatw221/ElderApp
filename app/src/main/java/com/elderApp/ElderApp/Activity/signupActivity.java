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
import android.widget.TextView;

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

public class signupActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ImageButton signup_back;
    private TextView signup_pass_name;
    private ZXingScannerView signup_qrscanner;
    private ConstraintLayout signup_qrscanner_layout,signup_pass_layout;
    private String url1="https://www.happybi.com.tw/api/isUserArrive/";
    private String url2="https://www.happybi.com.tw/api/arriveEvent/";
    private String slug,name;
    private Context context=this;
    private Activity activity=this;
    private ZXingScannerView.ResultHandler resultHandler=this;
    private String TheEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        slug=(String)getIntent().getExtras().get("slug");
        name=(String)getIntent().getExtras().get("name");
        signup_back=findViewById(R.id._signup_back);
        signup_pass_name=findViewById(R.id._signup_pass_name);
        signup_qrscanner=findViewById(R.id._signup_qrscanner);
        signup_qrscanner_layout=findViewById(R.id._signup_qrscanner_layout);
        signup_pass_layout=findViewById(R.id._signup_pass_layout);

        signup_back.setOnClickListener(btn_listener);

        JsonObjectRequest checkSigninRequest = new JsonObjectRequest(0, url1 + slug, null, RL, REL) {
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
        MySingleton.getInstance(context).getRequestQueue().add(checkSigninRequest);

//        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();


    }

    //---------------------回報Listener------------------------------------------------------------
    private Response.Listener RL = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONObject event = new JSONObject();
            try {
                int state=response.getInt("s");
                if(state==1){
                    signup_qrscanner_layout.setVisibility(View.GONE);
                    signup_pass_layout.setVisibility(View.VISIBLE);
                    signup_pass_name.setText(name);
                }else if(state==2){
                    signup_qrscanner_layout.setVisibility(View.VISIBLE);
                    signup_pass_layout.setVisibility(View.GONE);
                    Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();

                }else{

                }
            } catch (JSONException e) {

            }
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(context)
                    .setTitle("連線錯誤")
                    .show();
            finish();
            //
        }
    };

    @Override
    public void handleResult(Result rawResult) {
        String[] result=rawResult.getText().split(",");
        if(result[0].equals("arrive")){
            JsonObjectRequest signinRequest = new JsonObjectRequest(1, url2 + result[1], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getInt("s")==1){
                            TheEventName=response.getString("name");
                            signup_qrscanner_layout.setVisibility(View.GONE);
                            signup_pass_layout.setVisibility(View.VISIBLE);
                            signup_pass_name.setText(TheEventName);
                        }else{
                            new AlertDialog.Builder(context).setMessage(response.getString("m")).show();
                            signup_qrscanner.startCamera();
                        }
                    }catch (JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(context).setMessage("連線錯誤").show();
                    signup_qrscanner.startCamera();
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
            signup_qrscanner.startCamera();
        }

    }


    private PermissionListener permissionListener = new PermissionListener() {

        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
            signup_qrscanner.setResultHandler(resultHandler);
            signup_qrscanner.startCamera();
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {

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