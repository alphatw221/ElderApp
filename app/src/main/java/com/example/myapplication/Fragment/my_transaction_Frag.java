package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Model_Class.User;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.example.myapplication.Helper_Class.my_transaction_listview_adapter;

import org.json.JSONArray;


public class my_transaction_Frag extends Fragment {
    private ListView my_transaction_listview;
    private String url="https://www.happybi.com.tw/api/trans-history/";
    private Context context;
    private User user;
    private ImageButton backButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_my_transaction,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        my_transaction_listview=(ListView)view.findViewById(R.id._my_transaction_listview);
        backButton = view.findViewById(R.id._my_transaction_back);

        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        user=(User) getActivity().getIntent().getSerializableExtra("User");
        backButton.setOnClickListener(backListener);
        //---------------------發出請求------------------------------------------------------------
        myJsonRequest.GET_Request.getJSON_array(url+user.user_id,null,null,getActivity().getApplicationContext(),RL_JA,REL);
        //----------------------------------------------------------------------------------------------------------------------------------------------------

        return view;
    }
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL_JA=new Response.Listener<JSONArray>(){
        @Override
        public void onResponse(JSONArray response) {
        my_transaction_listview_adapter adapter=new my_transaction_listview_adapter(context,response);
        my_transaction_listview.setAdapter(adapter);


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




    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("frag1") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("frag1")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new Frag1(), "frag1").commit();
            }
            if(fragmentManager.findFragmentByTag("myTrans") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("myTrans")).commit();
//                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("give_money")).commit();
            }
        }
    };
}
