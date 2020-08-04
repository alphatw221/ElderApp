package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateMyDataActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private EditText update_name,update_cellPhone,update_tel,update_address,update_idcode;
    private Button update_comfirm,update_cancel;
    private Context context;
    //----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_data);
        //---------------------發出請求------------------------------------------------------------
        String url="https://www.happybi.com.tw/api/auth/myAccount";
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,this.getApplicationContext(),RL,REL).Fire();
        myJsonRequest.POST_Request.getJSON_object(url,key,value,this.getApplicationContext(),RL,REL);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        update_name=(EditText)findViewById(R.id._update_name);
        update_cellPhone=(EditText)findViewById(R.id._update_cellPhone);
        update_tel=(EditText)findViewById(R.id._update_tel);
        update_address=(EditText)findViewById(R.id._update_address);
        update_idcode=(EditText)findViewById(R.id._update_idcode);
        update_comfirm=(Button)findViewById(R.id._update_comfirm);
        update_cancel=(Button)findViewById(R.id._update_cancel);
        //-----------------初始設定----------------------------------------------------------------
        update_comfirm.setOnClickListener(comfirm_listener);
        update_cancel.setOnClickListener(cancel_listener);
        context=this.getApplicationContext();

    }
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                update_name.setText(response.getString("name"));
                update_cellPhone.setText(response.getString("phone"));
                update_tel.setText(response.getString("tel"));
                update_address.setText(response.getString("address"));
                update_idcode.setText(response.getString("id_number"));
            }catch (JSONException e){
                new AlertDialog.Builder(UpdateMyDataActivity.this)
                        .setTitle("錯誤")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(e.toString())
                        .show();
            }
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(UpdateMyDataActivity.this)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("系統錯誤")
                    .show();
            Intent intent = new Intent();
            intent.setClass(UpdateMyDataActivity.this, TabActivity.class);
            startActivity(intent);
            finish();
            //
        }
    };
    //---------------------確認修改按鈕Listener------------------------------------------------------------
    private  Button.OnClickListener comfirm_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            String url="https://www.happybi.com.tw/api/auth/updateAccount";
            Object[] key=new Object[]{"token","name","phone","tel","address","id_number"};
            Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token",""),
            update_name.getText().toString(),
            update_cellPhone.getText().toString(),
            update_tel.getText().toString(),
            update_address.getText().toString(),
            update_idcode.getText().toString()};
//            new myJsonRequest(url,"post",key,value,context,RL2,REL2).Fire();
            myJsonRequest.POST_Request.getJSON_object(url,key,value,context,RL2,REL2);
        }
    };

    //---------------------取消按鈕Listener------------------------------------------------------------
    private  Button.OnClickListener cancel_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

//            Intent intent = new Intent();
//            intent.setClass(UpdateMyDataActivity.this,TabActivity.class);
//            startActivity(intent);
            finish();
        }
    };



    //---------------------回報2Listener------------------------------------------------------------
    private  Response.Listener RL2=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                if(response.getString("s").equals("1")){
                    new AlertDialog.Builder(UpdateMyDataActivity.this)
                            .setTitle("更新成功")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(response.getString("m"))
                            .show();
//                    Intent intent = new Intent();
//                    intent.setClass(UpdateMyDataActivity.this,TabActivity.class);
//                    startActivity(intent);
                    finish();
                }else{
                    new AlertDialog.Builder(UpdateMyDataActivity.this)
                            .setTitle("更新失敗")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(response.getString("m"))
                            .show();
                }
            }catch (JSONException e){}
        }
    };
    //---------------------錯誤回報2Listener------------------------------------------------------------
    private Response.ErrorListener REL2=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(UpdateMyDataActivity.this)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("系統錯誤")
                    .show();
//            Intent intent = new Intent();
//            intent.setClass(UpdateMyDataActivity.this,TabActivity.class);
//            startActivity(intent);
            finish();
            //
        }
    };


}
