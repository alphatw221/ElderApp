package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class RegistrationActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    //-------------------宣告全域變數----------------------------------------------------------------------------------------------------------------------------------
    private Spinner District_Spinner,how2Pay_Spinner;
    private EditText account,PW,PWcomfirm,name,cellPhone,phone,idCode,address,inviter;
    private TextView account_error,PW_error,PWcomfirm_error,
            name_error,cellPhone_error,gender_error,idCode_error,
            district_error,address_error,how2Pay_error,inviter_textview;
    private RadioButton male,female;
    private RadioGroup genderGroup;
    private Button go_back,submit,show_qr_btn;
    private DatePicker birthday_datePicker;
    private ZXingScannerView qrscanner;
    private ConstraintLayout qr_layout;
    String[]test={"請選擇地區","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
    String[] how2Pay={"請選擇付款方式","推薦人代收","自行繳費"};
    String url="https://www.happybi.com.tw/api/inviterCheck";
    String url2="https://www.happybi.com.tw/api/member/join";
    private SharedPreferences preference;
    private Context context;
    private Activity activity;
    private ZXingScannerView.ResultHandler resultHandler=this;
    private String inviter_name;
    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //-------------------開始抓取物件----------------------------------------------------------------------------------------------------------------------------------
        go_back=(Button) findViewById(R.id._return);
        submit=(Button)findViewById(R.id._subbmit);
        show_qr_btn=(Button)findViewById(R.id._show_qr_btn);

        genderGroup=(RadioGroup) findViewById(R.id._genderGroup);
        male=(RadioButton) findViewById(R.id._male);
        female=(RadioButton) findViewById(R.id._female);

        District_Spinner=(Spinner) findViewById(R.id._district);
        how2Pay_Spinner=(Spinner)findViewById(R.id._how2pay);
        birthday_datePicker=(DatePicker) findViewById(R.id._birthday);

        account=(EditText) findViewById(R.id._account);
        PW=(EditText)findViewById(R.id._PW);
        PWcomfirm=(EditText)findViewById(R.id._PWcomfirm);
        name=(EditText)findViewById(R.id._person_name);
        cellPhone=(EditText)findViewById(R.id._cellPhone);
        phone=(EditText)findViewById(R.id._phone);
        idCode=(EditText)findViewById(R.id._idCode);
        address=(EditText)findViewById(R.id._address);
        inviter=(EditText)findViewById(R.id._inviter);

        account_error=(TextView)findViewById(R.id._account_error);
        PW_error=(TextView)findViewById(R.id._PW_error);
        PWcomfirm_error=(TextView)findViewById(R.id._PWcomfirm_error);
        name_error=(TextView)findViewById(R.id._name_error);
        cellPhone_error=(TextView)findViewById(R.id._cellPhone_error);
        gender_error=(TextView)findViewById(R.id._gender_error);
        idCode_error=(TextView)findViewById(R.id._idCode_error);
        district_error=(TextView)findViewById(R.id._district_error);
        address_error=(TextView)findViewById(R.id._address_error);
        how2Pay_error=(TextView)findViewById(R.id._how2pay_error);
        inviter_textview=(TextView)findViewById(R.id._inviter_textview);

        qrscanner=findViewById(R.id._registration_qrscanner);
        qr_layout=findViewById(R.id._qr_layout);
        //--------------------初始設定---------------------------------------------------------------------------------------------------------------------------------
        ArrayAdapter<String> adapterTest=new ArrayAdapter<String>(this,R.layout.registration_spinner_layout,test);
        ArrayAdapter<String> adapter_how2Pay=new ArrayAdapter<String>(this,R.layout.registration_spinner_layout,how2Pay);

        adapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_how2Pay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        District_Spinner.setAdapter(adapterTest);
        how2Pay_Spinner.setAdapter(adapter_how2Pay);
        District_Spinner.setOnItemSelectedListener(DistrictListener);
        how2Pay_Spinner.setOnItemSelectedListener(how2PayListener);

        go_back.setOnClickListener(go_backListener);
        submit.setOnClickListener(submitListener);
        show_qr_btn.setOnClickListener(show_qr_Listener);


        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        context=this;
        activity=this;
    }

    //-----------------------------顯示qr_Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener show_qr_Listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
//            qrscanner.setVisibility(View.VISIBLE);
//            qrscanner.setResultHandler(resultHandler);
            qr_layout.setVisibility(View.VISIBLE);
            Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
//            qrscanner.startCamera();
//            FragmentManager FM=getSupportFragmentManager();
//            FragmentTransaction FT=FM.beginTransaction();
//            FT.replace(R.id._registration_qrscanner,new qrscanner_text(),"qrscanner").commit();
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
//        qrscanner.startCamera();
//    }

    @Override
    public void handleResult(Result rawResult) {
        inviter.setText(rawResult.getText());
        qrscanner.stopCamera();
        qr_layout.setVisibility(View.GONE);
    }

    private PermissionListener permissionListener=new PermissionListener() {
        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
//            qrscanner.setResultHandler(RegistrationActivity.this);
            qrscanner.setResultHandler(resultHandler);
            qrscanner.startCamera();
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {
            qr_layout.setVisibility(View.GONE);
        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

        }
    };



//-------------------------------地區下拉Listener----------------------------------------------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener DistrictListener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String _district=parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
//-----------------------------付款方式下拉Listener------------------------------------------------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener how2PayListener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String _how2Pay=parent.getSelectedItem().toString();
            if(_how2Pay.equals("推薦人代收")){
                inviter_textview.setVisibility(View.VISIBLE);
                inviter.setVisibility(View.VISIBLE);
                show_qr_btn.setVisibility(View.VISIBLE);
            }else{
                inviter_textview.setVisibility(View.GONE);
                inviter.setVisibility(View.GONE);
                show_qr_btn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
//-----------------------------送出按鈕Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener submitListener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(verification()){
                if(how2Pay_Spinner.getSelectedItemPosition()==1){//推薦人
                    //-----------------------驗證推薦人----------------------------------------

                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url+"?inviter_id_code="+inviter.getText().toString(),null,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("s")==1){
                                    inviter_name=response.getString("inviter");
//                                    //--------------------驗證完成送出註冊請求------------------------------------------------------------
                                    int gender_int;
                                    if(male.isChecked())gender_int=1;else gender_int=0;
                                    String birthday_Date=Integer.toString(birthday_datePicker.getYear()) + "-" + Integer.toString(birthday_datePicker.getMonth()) + "-" + Integer.toString(birthday_datePicker.getDayOfMonth());
                                    JSONObject jsonObject=new JSONObject();
                                    try{
                                        jsonObject.put("id_number",idCode.getText().toString());
                                        jsonObject.put("name",name.getText().toString());
                                        jsonObject.put("email",account.getText().toString());
                                        jsonObject.put("password",PW.getText().toString());
                                        jsonObject.put("gender",gender_int);
                                        jsonObject.put("birthdate",birthday_Date);
                                        jsonObject.put("phone",cellPhone.getText().toString());
                                        jsonObject.put("tel",phone.getText().toString());
                                        jsonObject.put("address",address.getText().toString());
                                        jsonObject.put("district_id",District_Spinner.getSelectedItemPosition());
                                        jsonObject.put("pay_method",how2Pay_Spinner.getSelectedItemPosition());
                                        jsonObject.put("app",true);
                                        jsonObject.put("inviter",inviter_name);
                                        jsonObject.put("inviter_id_code",inviter.getText().toString());
                                    }catch (JSONException e){ }
                                    JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(1, url2,jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if(response.getInt("s")==1){
                                                    new AlertDialog.Builder(context).setMessage("已完成會員註冊").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    }).show();
                                                }else{
                                                    new AlertDialog.Builder(context).setMessage("註冊失敗，請從新註冊或聯繫客服人員").show();
                                                }
                                            }catch (JSONException e){ }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            String m;
                                            try{
                                                m=new String(error.networkResponse.data,"UTF-8");
                                                new AlertDialog.Builder(context)
                                                        .setTitle("註冊失敗")
                                                        .setMessage(m)
                                                        .show();
                                            }catch (UnsupportedEncodingException e){}
                                        }
                                    }){
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> headers = new HashMap<String, String>();
                                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                                            headers.put("Content-Type", "application/json");
                                            headers.put("Accept","application/json");
                                            return headers;
                                        }
                                    };
                                    MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest1);
                                }else{
                                    //推薦人資料有誤
                                    new AlertDialog.Builder(context).setMessage("註冊失敗:推薦人資料有誤").show();
                                }
                            }catch (JSONException e){ }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String m;
                            try{
                                m=new String(error.networkResponse.data,"UTF-8");
                                new AlertDialog.Builder(context)
                                        .setMessage(m)
                                        .show();
                            }catch (UnsupportedEncodingException e){}
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                            headers.put("Content-Type", "application/json");
                            headers.put("Accept","application/json");
                            return headers;
                        }
                    };
                    MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);

                    //------------------------------------------------------------------------
                }else{
                    //直接註冊
                    int gender_int;
                    if(male.isChecked())gender_int=1;else gender_int=0;
                    String birthday_Date=Integer.toString(birthday_datePicker.getYear()) + "-" + Integer.toString(birthday_datePicker.getMonth()) + "-" + Integer.toString(birthday_datePicker.getDayOfMonth());
                    JSONObject jsonObject=new JSONObject();
                    try{
                        jsonObject.put("id_number",idCode.getText().toString());
                        jsonObject.put("name",name.getText().toString());
                        jsonObject.put("email",account.getText().toString());
                        jsonObject.put("password",PW.getText().toString());
                        jsonObject.put("gender",gender_int);
                        jsonObject.put("birthdate",birthday_Date);
                        jsonObject.put("phone",cellPhone.getText().toString());
                        jsonObject.put("tel",phone.getText().toString());
                        jsonObject.put("address",address.getText().toString());
                        jsonObject.put("app",true);
                        jsonObject.put("district_id",District_Spinner.getSelectedItemPosition());
                        jsonObject.put("pay_method",how2Pay_Spinner.getSelectedItemPosition());

//                        jsonObject.put("inviter",inviter_name);
//                        jsonObject.put("inviter_id_code",inviter);
                    }catch (JSONException e){ }

                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1,url2,jsonObject,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("s")==1){
                                    new AlertDialog.Builder(context).setMessage("已完成會員註冊").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).show();
                                }else{
                                    new AlertDialog.Builder(context).setMessage("註冊失敗，請從新註冊或聯繫客服人員").show();
                                }
                            }catch (JSONException e){ }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String m;
                            try{
                                m=new String(error.networkResponse.data,"UTF-8");
                                new AlertDialog.Builder(context)
                                        .setTitle("註冊失敗")
                                        .setMessage(m)
                                        .show();
                            }catch (UnsupportedEncodingException e){}


                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                            headers.put("Content-Type", "application/json");
                            headers.put("Accept","application/json");
                            return headers;
                        }

                    };
                    MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
                }
            }else{
                new AlertDialog.Builder(context).setMessage("部分資料有誤 請完成修改後重試").show();
            }

        }



        //---------------------驗證副程式------------------------------------------------------------
        private boolean verification(){
            boolean Flag=true;
            if(TextUtils.isEmpty(account.getText().toString())){
                account_error.setText("此欄位必填");
                Flag=false;
            }else account_error.setText("");
            if(TextUtils.isEmpty(PW.getText().toString())){
                PW_error.setText("此欄位必填");
                Flag=false;
            }else{
                PW_error.setText("");
                if(PWcomfirm.getText().toString().equals(PW.getText().toString())){
                    PWcomfirm_error.setText("");
                }else {
                    PWcomfirm_error.setText("密碼不相符");
                    Flag=false;
                }
            }
            if(TextUtils.isEmpty(name.getText().toString())){
                name_error.setText("此欄位必填");
            }else name_error.setText("");
            if(TextUtils.isEmpty(idCode.getText().toString())){
                idCode_error.setText("此欄位必填");
                Flag=false;
            }else idCode_error.setText("");
            if(TextUtils.isEmpty(address.getText().toString())){
                address_error.setText("此欄位必填");
                Flag=false;
            }else address_error.setText("");
            if(genderGroup.getCheckedRadioButtonId()==-1){
                gender_error.setText("請選擇性別");
                Flag=false;
            }else gender_error.setText("");
            if(District_Spinner.getSelectedItemPosition()==0){
                district_error.setText("請選擇地區");
                Flag=false;
            }else district_error.setText("");
            if(how2Pay_Spinner.getSelectedItemPosition()==0){
                how2Pay_error.setText("請選擇付款方式");
                Flag=false;
            }else how2Pay_error.setText("");
            return Flag;
        }
    };
//-----------------------------返回按鈕Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener go_backListener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent();
//            intent.setClass(RegistrationActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
    };


}
