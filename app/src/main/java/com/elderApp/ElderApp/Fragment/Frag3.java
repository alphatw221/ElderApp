package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.MainActivity;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.QRCodeHelper;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Activity.UpdateMyDataActivity;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Frag3 extends Fragment {
    private EditText myAccount_name,myAccount_account,myAccount_gender,
            myAccount_birthday,myAccount_cellPhone,myAccount_tel,myAccount_address,myAccount_idcode;
    private Button myAccount_update,myAccount_apply,myAccount_agrement,myAccount_logout;
    private TextView myAccount_member,myAccount_valid,myAccount_expiry_date;
    private ImageView myAccount_qr,myAccount_image;
    private String url="https://www.happybi.com.tw/api/auth/myAccount";
    private String url2="https://www.happybi.com.tw/api/extendMemberShip";
    private SharedPreferences preferences;
    private boolean firstset=false;
    private Context context;
    private int user_id;
    private ImageButton myAccount_image_edit;
    private Bitmap bitmap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag3_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        myAccount_name=(EditText)view.findViewById(R.id._myAccount_name);
        myAccount_account=(EditText)view.findViewById(R.id._myAccount_account);
        myAccount_gender=(EditText)view.findViewById(R.id._myAccount_gender);
        myAccount_birthday=(EditText)view.findViewById(R.id._myAccount_birthday);
        myAccount_cellPhone=(EditText)view.findViewById(R.id._myAccount_cellPhone);
        myAccount_tel=(EditText)view.findViewById(R.id._myAccount_tel);
        myAccount_address=(EditText)view.findViewById(R.id._myAccount_address);
        myAccount_idcode=(EditText)view.findViewById(R.id._myAccount_idcode);
        myAccount_qr=(ImageView) view.findViewById(R.id._myAccount_qr);
        myAccount_update=(Button)view.findViewById(R.id._myAccount_update);
        myAccount_agrement=(Button)view.findViewById(R.id._myAccount_agrement);
        myAccount_logout=(Button)view.findViewById(R.id._myAccount_logout);
        myAccount_apply=(Button)view.findViewById(R.id._myAccount_apply);
        myAccount_valid=view.findViewById(R.id._myAccount_valid);
        myAccount_expiry_date=view.findViewById(R.id._myAccount_expiry_date);
        myAccount_image_edit=view.findViewById(R.id._myAccount_image_edit);
        myAccount_image=view.findViewById(R.id._myAccount_image);
        //---------------------發出請求------------------------------------------------------------
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,getActivity().getApplicationContext(),RL,REL).Fire();
        myJsonRequest.POST_Request.getJSON_object(url,key,value,getActivity().getApplicationContext(),RL,REL);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        myAccount_logout.setOnClickListener(logout_listener);
        myAccount_agrement.setOnClickListener(agreement_listener);
        myAccount_update.setOnClickListener(update_listener);
        myAccount_apply.setOnClickListener(apply_listener);
        myAccount_image_edit.setOnClickListener(image_edit_listener);
        preferences=this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE);
        context=this.getContext();
        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                myAccount_name.setText(response.getString("name"));
                myAccount_account.setText(response.getString("email"));
                if(response.getString("gender").equals("1")){
                    myAccount_gender.setText("男性");
                }else myAccount_gender.setText("女性");
                myAccount_birthday.setText(response.getString("birthdate"));
                myAccount_cellPhone.setText(response.getString("phone"));
                myAccount_tel.setText(response.getString("tel"));
                myAccount_address.setText(response.getString("address"));
                myAccount_idcode.setText(response.getString("id_number"));
                if(response.getString("expiry_date")==null){
                    myAccount_expiry_date.setText("---");
                }else{
                    myAccount_expiry_date.setText(response.getString("expiry_date"));
                }

                if(response.getInt("valid")==0){
                    myAccount_valid.setText("代付款");
                    myAccount_valid.setTextColor(Color.parseColor("#FF0000"));
                }else {
                    myAccount_valid.setText("有效");
                    myAccount_valid.setTextColor(Color.parseColor("#76FF03"));
                }
                if(response.getString("img")!=null){
                    Picasso.get().load(response.getString("img")).into(myAccount_image);
                }

                Bitmap bitmap = QRCodeHelper
                        .newInstance(context)
                        .setContent(response.getString("id_code"))
                        .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                        .setMargin(1)
                        .getQRCOde();
                myAccount_qr.setImageBitmap(bitmap);
                firstset=true;

                user_id=response.getInt("id");
            }catch(JSONException e){
                new AlertDialog.Builder(context)
                        .setMessage("JSON錯誤")
                        .show();
            }
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(error.toString())
                    .show();
        }
    };
    //-----------------編輯相片Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener image_edit_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
        }
    };

    //---------------------------------------------------------
    private String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),path);
            }catch (IOException e){ }
            String url3="https://www.happybi.com.tw/api/auth/uploadImage";
            JSONObject jsonObject=new JSONObject();
            try{
                jsonObject.put("image",bitmap2String(bitmap));
            }catch (JSONException e){}
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1,url3,jsonObject,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try{
                        if(response.getString("status").equals("success")){
                            Picasso.get().load(response.getString("imgUrl")).resize(100,100).centerCrop().into(myAccount_image);
                        }else{
                            new AlertDialog.Builder(context)
                                    .setMessage("連線失敗請重試")
                                    .show();
                        }
                    }catch (JSONException e){}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(context)
                            .setTitle("連線錯誤")
                            .setMessage("請重新登入")
                            .show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization","Bearer "+Token);
                    return headers;
                }

            };
            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);

        }
    }

    //-----------------修改資料按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener update_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UpdateMyDataActivity.class);
            startActivity(intent);
//            getActivity().finish();
        }
    };

    //-----------------申請續會按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener apply_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("user_id",user_id);
            }catch (JSONException e){

            }

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1, url2, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray myEventList=new JSONArray();
                    try {
                        new AlertDialog.Builder(context).setMessage(response.getString("m")).show();
                    }catch (JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new AlertDialog.Builder(context)
                            .setTitle("連線錯誤")
                            .setMessage("請重新登入")
                            .show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization","Bearer "+Token);
                    return headers;
                }
            };

            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);

        }
    };

    //-----------------服務條款按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener agreement_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };
    //-----------------登出按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener logout_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            preferences.edit().remove("access_token").commit();
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if(firstset){
            Object[] key=new Object[]{"token"};
            Object[] value=new Object[]{this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,getActivity().getApplicationContext(),RL,REL).Fire();
            myJsonRequest.POST_Request.getJSON_object(url,key,value,getActivity().getApplicationContext(),RL,REL);
        }
    }
}
