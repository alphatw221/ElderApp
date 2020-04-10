package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.Helper_Class.MySingleton;
import com.example.myapplication.Helper_Class.market_listview_adapter;
import com.example.myapplication.Helper_Class.market_myOrder_listview_adapter;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.event_listview_adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class market_Frag extends Fragment {

    private ListView market_listView;
    private Button market_product,market_myProduct;
    private ImageButton market_back;
    private Context context;
    private String Token;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_market_,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        market_listView=(ListView)view.findViewById(R.id._market_listView);
        market_product=(Button)view.findViewById(R.id._market_product);
        market_myProduct=(Button)view.findViewById(R.id._market_myProduct);
        market_back=(ImageButton)view.findViewById(R.id._market_back);
        Token=this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        market_back.setOnClickListener(backListener);
        market_listView.setOnItemClickListener(click_listener);
        market_product.setBackgroundColor(Color.parseColor("#FF6D00"));

        market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        market_myProduct.setOnClickListener(button_listener);
        //---------------------發出請求------------------------------------------------------------
        String url1="https://www.happybi.com.tw/api/getAllProduct";

        myJsonRequest.GET_Request.getJSON_object(url1,null,null,getActivity().getApplicationContext(),RL1,REL1);


        //----------------------------------------------------------------------------------------------------------------------------------------------------








        return view;
    }

    //---------------------回報Listener1------------------------------------------------------------
    private  Response.Listener RL1=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            market_listview_adapter market_listview_adapter =new market_listview_adapter(context,response);
            market_listView.setAdapter(market_listview_adapter);
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

    //--------------------------------------------返回Listener--------------------------------------------------------
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
            if(fragmentManager.findFragmentByTag("market") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("market")).commit();
//                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("give_money")).commit();
            }
        }
    };
    //---------------------------------------listview_click Listener---------------------------------------------------
    private ListView.OnItemClickListener click_listener= new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Object object=parent.getItemAtPosition(position);
            Product_class product_class=(Product_class)object;

            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            if(fragmentManager.findFragmentByTag("product_detail") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("product_detail")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new product_detail_Frag(product_class), "product_detail").commit();
            }
            if(fragmentManager.findFragmentByTag("market") != null){
                //if the other fragment is visible, hide it.
//                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("market")).commit();
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("market")).commit();
            }
        }

    };
    //---------------------------------------按鈕 Listener---------------------------------------------------
    private Button.OnClickListener button_listener= new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._market_product:
                    market_product.setBackgroundColor(Color.parseColor("#FF6D00"));
                    market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_myProduct.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(click_listener);


                    String url1="https://www.happybi.com.tw/api/getAllProduct";

                    myJsonRequest.GET_Request.getJSON_object(url1,null,null,getActivity().getApplicationContext(),RL1,REL1);


                    market_product.setOnClickListener(null);
                    break;
                case R.id._market_myProduct:
                    market_product.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_myProduct.setBackgroundColor(Color.parseColor("#FF6D00"));
                    market_product.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(null);

                    String url2="https://www.happybi.com.tw/api/my-order-list";
                    JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(0, url2, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            market_myOrder_listview_adapter market_myOrder_listview_adapter =new market_myOrder_listview_adapter(context,response);
                            market_listView.setAdapter(market_myOrder_listview_adapter);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization","Bearer "+Token);
                            return headers;
                        }
                    };
                    MySingleton.getInstance(context).getRequestQueue().add(jsonArrayRequest);


                    market_myProduct.setOnClickListener(null);
                    break;
            }
        }
    };

}
