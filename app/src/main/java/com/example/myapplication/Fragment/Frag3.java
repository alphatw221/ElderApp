package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Activity.UpdateMyDataActivity;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Frag3 extends Fragment {
    private EditText myAccount_name,myAccount_account,myAccount_gender,
            myAccount_birthday,myAccount_cellPhone,myAccount_tel,myAccount_address,myAccount_idcode;
    private Button myAccount_update,myAccount_apply,myAccount_agrement,myAccount_logout;
    private TextView myAccount_member;
    private String url="https://www.happybi.com.tw/api/auth/myAccount";
    private SharedPreferences preferences;
    private Context context;


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
        myAccount_member=(TextView)view.findViewById(R.id._myAccount_member);
        myAccount_update=(Button)view.findViewById(R.id._myAccount_update);
        myAccount_agrement=(Button)view.findViewById(R.id._myAccount_agrement);
        myAccount_logout=(Button)view.findViewById(R.id._myAccount_logout);
        myAccount_apply=(Button)view.findViewById(R.id._myAccount_apply);
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
            }catch(JSONException e){
                new AlertDialog.Builder(getActivity())
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
            new AlertDialog.Builder(getActivity())
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(error.toString())
                    .show();
        }
    };
    //-----------------修改資料按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener update_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UpdateMyDataActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };

    //-----------------申請續會按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener apply_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
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
}
