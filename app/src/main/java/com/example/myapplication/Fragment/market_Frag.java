package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.Helper_Class.MySingleton;
import com.example.myapplication.Helper_Class.jasonList_2_objList;
import com.example.myapplication.Helper_Class.market_listview_adapter;
import com.example.myapplication.Helper_Class.market_myOrder_listview_adapter;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.example.myapplication.Model_Class.Order_class;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.Model_Class.Transaction_class;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.event_listview_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class market_Frag extends Fragment {

    private ListView market_listView;
    private Button market_product,market_myProduct;
    private ImageButton market_back;
    private Context context;
    private String Token,url1,url2;
    private int page=1;
    private boolean hasNextPage;
    private List<Product_class> list;
    private List<Order_class>list2;
    private market_listview_adapter market_listview_adapter;
    market_myOrder_listview_adapter market_myOrder_listview_adapter;
    private String btn_flag;
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

//
//        market_product.setBackgroundColor(Color.parseColor("#FF6D00"));
//
//        market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        btn_flag="left";
        market_myProduct.setOnClickListener(button_listener);
        //---------------------發出請求------------------------------------------------------------
        url1="https://www.happybi.com.tw/api/productList";
        url2="https://www.happybi.com.tw/api/order/myOrderList";
//        myJsonRequest.GET_Request.getJSON_object(url1,null,null,getActivity().getApplicationContext(),RL1,REL1);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url1+"?page="+page,null,RL1,REL1){
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
        //----------------------------------------------------------------------------------------------------------------------------------------------------

        return view;
    }

    //---------------------回報Listener1------------------------------------------------------------
    private  Response.Listener RL1=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {

            JSONArray jsonArray=new JSONArray();
            try {
                jsonArray=response.getJSONArray("productList");
                hasNextPage=response.getBoolean("hasNextPage");
            }catch (JSONException e){
                Log.d("json error",e.toString());
            }
//            List<Product_class> t=new ArrayList<Product_class>();

            list= jasonList_2_objList.convert_2_Product_list(context,jsonArray);

            market_listview_adapter =new market_listview_adapter(context,list);

            market_listView.setAdapter(market_listview_adapter);
            market_listView.setOnScrollListener(onScrollListener);
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

    //-------------------------------------滑動自動更新----------------------------------------------------------------
    private ListView.OnScrollListener onScrollListener=new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if(hasNextPage){

                if(firstVisibleItem>(totalItemCount-5)){
                    if(btn_flag.equals("left")){
                        page++;
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url1+"?page="+page,null,RL1_2,REL1_2){
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
                        market_listView.setOnScrollListener(null);
                    }else{
                        page++;
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url2+"?page="+page,null,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONArray jsonArray=new JSONArray();
                                try {
                                    jsonArray=response.getJSONArray("orderList");
                                    hasNextPage=response.getBoolean("hasNextPage");
                                }catch (JSONException e){
                                    Log.d("json error",e.toString());
                                }
                                List<Order_class> t=new ArrayList<Order_class>();

                                t= jasonList_2_objList.convert_2_Order_list(context,jsonArray);
                                list2.addAll(t);
                                market_myOrder_listview_adapter.notifyDataSetChanged();
                                market_listView.setOnScrollListener(onScrollListener);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

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
                        market_listView.setOnScrollListener(null);
                    }

                }
            }

        };
    };


    //---------------------回報Listener1_2------------------------------------------------------------
    private  Response.Listener RL1_2=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {

            JSONArray jsonArray=new JSONArray();
            try {
                jsonArray=response.getJSONArray("productList");
                hasNextPage=response.getBoolean("hasNextPage");
            }catch (JSONException e){
                Log.d("json error",e.toString());
            }
            List<Product_class> t=new ArrayList<Product_class>();

            t= jasonList_2_objList.convert_2_Product_list(context,jsonArray);
            list.addAll(t);
            
            market_listview_adapter.notifyDataSetChanged();
            market_listView.setAdapter(market_listview_adapter);
        }
    };
    //---------------------錯誤回報Listener1------------------------------------------------------------
    private Response.ErrorListener REL1_2=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(error.toString())
                    .show();
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

            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("product_detail_Frag");
            Fragment fragment2=FM.findFragmentByTag("market_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);

                } else {
                    FT.add(R.id._fragment_frag1_blank, fragment, "product_detail_Frag");

                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new product_detail_Frag(product_class),"product_detail_Frag");
            }
            FT.commit();
        }

    };


    //---------------------------------------按鈕 Listener---------------------------------------------------
    private Button.OnClickListener button_listener= new Button.OnClickListener()
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._market_product:
                    btn_flag="left";
                    page=1;
//                    market_product.setBackgroundColor(Color.parseColor("#FF6D00"));
//                    market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_product.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_myProduct.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(click_listener);
                    market_product.setOnClickListener(null);
//                  //--
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url1+"?page="+page,null,RL1,REL1){
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
                    //--
                    break;

                case R.id._market_myProduct:
                    btn_flag="right";
                    page=1;
                    market_myProduct.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_product.setBackgroundColor(Color.parseColor("#00FFFFFF"));
//                    market_product.setBackgroundColor(Color.parseColor("#00FFFFFF"));
//                    market_myProduct.setBackgroundColor(Color.parseColor("#FF6D00"));
                    market_product.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(null);
                    market_myProduct.setOnClickListener(null);



                    JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(0, url2, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            JSONArray jsonArray=new JSONArray();
                            try {
                                jsonArray=response.getJSONArray("orderList");
                                hasNextPage=response.getBoolean("hasNextPage");
                            }catch (JSONException e){
                                Log.d("json error",e.toString());
                            }
//            List<Product_class> t=new ArrayList<Product_class>();

                            list2= jasonList_2_objList.convert_2_Order_list(context,jsonArray);
                            market_myOrder_listview_adapter =new market_myOrder_listview_adapter(context,list2);
                            market_listView.setAdapter(market_myOrder_listview_adapter);
                            market_listView.setOnScrollListener(onScrollListener);
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
                    MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest2);



                    break;
            }
        }
    };


    //--------------------------------------------返回Listener--------------------------------------------------------
    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("Frag1");
            Fragment fragment2=FM.findFragmentByTag("market_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "market_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

            }
            FT.commit();
        }
    };

}
