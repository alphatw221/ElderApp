package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;

public class Frag2 extends Fragment {

    private ListView event_listview;
    private String url="https://www.happybi.com.tw/api/getEvents";
    private Context context;
    private ImageButton event_back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag2_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        event_listview=(ListView)view.findViewById(R.id._event_listview);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        //---------------------發出請求------------------------------------------------------------
        new myJsonRequest(url,"get",getActivity().getApplicationContext(),RL_JA,REL,0).Fire2();
        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }


    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL_JA=new Response.Listener<JSONArray>(){
        @Override
        public void onResponse(JSONArray response) {
                event_listview_adapter eventlistviewadapter =new event_listview_adapter(context,response);
                event_listview.setAdapter(eventlistviewadapter);
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



}
