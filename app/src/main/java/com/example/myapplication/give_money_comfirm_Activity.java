package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class give_money_comfirm_Activity extends AppCompatActivity {
    private Button give_money_comfirm_comfirm,give_money_comfirm_cancel;
    private EditText give_money_comfirm_amount,give_money_comfirm_message;
    private TextView give_money_object;
    private Context app_context;
    private String id,name,email,amount,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_money_comfirm_);

        give_money_comfirm_comfirm=(Button)findViewById(R.id._give_money_comfirm_cancel);
        give_money_comfirm_cancel=(Button)findViewById(R.id._give_money_comfirm_cancel);
        give_money_object=(TextView)findViewById(R.id._give_money_comfirm_object);
        give_money_comfirm_message=(EditText)findViewById(R.id._give_money_comfirm_message);
        give_money_comfirm_amount=(EditText)findViewById(R.id._give_money_comfirm_amount);

        give_money_comfirm_comfirm.setOnClickListener(comfirm_listener);
        give_money_comfirm_cancel.setOnClickListener(cancel_listener);
        give_money_object.setText(name);
        app_context=this.getApplicationContext();
    }

    //---------------------支付按鈕Listener------------------------------------------------------------
    private Button.OnClickListener comfirm_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            amount=give_money_comfirm_amount.toString();
            message=give_money_comfirm_message.toString();
            String url="https://www.happybi.com.tw/api/transaction";
            Object[] key=new Object[]{"give_id","give_email","take_id","take_email","amount"};
            Object[] value=new Object[]{
                    "",
                    "",
                    "",
                    "",
                    amount
            };
            new myJsonRequest(url,"post",key,value,app_context,RL,REL).Fire();
        }
    };
    //---------------------取消支付按紐Listener------------------------------------------------------------
    private Button.OnClickListener cancel_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            new AlertDialog.Builder(give_money_comfirm_Activity.this)
                    .setTitle("支付結果")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(response.toString())
                    .show();
            finish();

        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(give_money_comfirm_Activity.this)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("支付失敗")
                    .show();

            finish();
            //
        }
    };
}
