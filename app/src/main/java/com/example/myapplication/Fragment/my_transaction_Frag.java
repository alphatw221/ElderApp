package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        my_transaction_listview.setOnItemClickListener(null);
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
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("Frag1");
            Fragment fragment2=FM.findFragmentByTag("my_transaction_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._frag1_fragment, fragment, "my_transaction_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._frag1_fragment,new Frag1(),"Frag1");

            }
            FT.commit();
        }
    };
}
