package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;


public class market_Frag extends Fragment {

    public ListView market_listView;
    public Button market_product,market_myProduct;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= super.onCreateView(inflater, container, savedInstanceState);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        market_listView=(ListView)view.findViewById(R.id._market_listView);
        market_product=(Button)view.findViewById(R.id._market_product);
        market_myProduct=(Button)view.findViewById(R.id._market_myProduct);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        //---------------------發出請求------------------------------------------------------------
        String url1="";
        String url2="";
        new myJsonRequest(url1,"get",getActivity().getApplicationContext(),RL1,REL1).Fire();
        new myJsonRequest(url2,"get",getActivity().getApplicationContext(),RL2,REL2).Fire();
        //----------------------------------------------------------------------------------------------------------------------------------------------------








        return view;
    }

    //---------------------回報Listener1------------------------------------------------------------
    private  Response.Listener RL1=new Response.Listener<JSONArray>(){
        @Override
        public void onResponse(JSONArray response) {
            event_listview_adapter eventlistviewadapter =new event_listview_adapter(context,response);
            event_listview.setAdapter(eventlistviewadapter);
        }
    };
    //---------------------錯誤回報Listener1------------------------------------------------------------
    private Response.ErrorListener REL1=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(error.toString())
                    .show();
        }
    };

    //---------------------回報Listener2------------------------------------------------------------
    private  Response.Listener RL2=new Response.Listener<JSONArray>(){
        @Override
        public void onResponse(JSONArray response) {
            event_listview_adapter eventlistviewadapter =new event_listview_adapter(context,response);
            event_listview.setAdapter(eventlistviewadapter);
        }
    };
    //---------------------錯誤回報Listener2------------------------------------------------------------
    private Response.ErrorListener REL2=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(error.toString())
                    .show();
        }
    };
}
