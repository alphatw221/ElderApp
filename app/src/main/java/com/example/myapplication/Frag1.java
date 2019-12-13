package com.example.myapplication;

import android.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Frag1 extends Fragment {
    private TextView person_name,person_rank,person_happybi;
    private Button takeBi,giveBi,myTrans,exchange;
    private SharedPreferences preference;
    private String url="https://www.happybi.com.tw/api/auth/me";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag1_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        person_name=(TextView) view.findViewById(R.id._person_name);
        person_rank=(TextView) view.findViewById(R.id._person_rank);
        person_happybi=(TextView) view.findViewById(R.id._person_happybi);
        takeBi=(Button)view.findViewById(R.id._takeBi);
        giveBi=(Button)view.findViewById(R.id._giveBi);
        myTrans=(Button)view.findViewById(R.id._myTrans);
        exchange=(Button)view.findViewById(R.id._exchange);
        //---------------------發出請求------------------------------------------------------------
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
        new myJsonObjectRequest(url,"post",key,value,getActivity().getApplicationContext(),RL,REL).Fire();
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        takeBi.setOnClickListener(takeBi_listener);
        giveBi.setOnClickListener(giveBi_listener);
        myTrans.setOnClickListener(myTrans_listener);
        exchange.setOnClickListener(exchange_listener);

        //----------------------------------------------------------------------------------------------------------------------------------------------------


        return view;
    }

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
                try{
                    person_name.setText(response.getString("name"));
                    person_happybi.setText(response.getString("wallet"));
                    person_rank.setText(response.getString("rank"));
                }catch (JSONException e){

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
    //-----------------收樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener takeBi_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };

    //-----------------送樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener giveBi_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };

    //-----------------我的帳簿按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener myTrans_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };
    //-----------------商品兌換按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener exchange_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };
}
