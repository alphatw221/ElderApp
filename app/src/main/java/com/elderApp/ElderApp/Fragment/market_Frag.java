package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Helper_Class.market_listview_adapter;
import com.elderApp.ElderApp.Helper_Class.market_myOrder_listview_adapter;
import com.elderApp.ElderApp.Model_Class.Order_class;
import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.R;

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
    private String Token;
    private int page=1;
    private boolean hasNextPage;
    private List<Product_class> list;
    private List<Order_class>list2;
    private market_listview_adapter market_listview_adapter;
    market_myOrder_listview_adapter market_myOrder_listview_adapter;
    private String btn_flag;
    private ProgressBar market_progressbar;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_market_,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        market_listView=(ListView)view.findViewById(R.id._market_listView);
        market_product=(Button)view.findViewById(R.id._market_product);
        market_myProduct=(Button)view.findViewById(R.id._market_myProduct);
        market_back=(ImageButton)view.findViewById(R.id._market_back);
        Token=this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
        market_progressbar=view.findViewById(R.id._market_progressbar);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        market_back.setOnClickListener(backListener);
        market_listView.setOnItemClickListener(click_listener);
        btn_flag="left";
        market_myProduct.setOnClickListener(button_listener);
        //---------------------發出請求------------------------------------------------------------
        get_productList();
        return view;
    }
    private void get_productList(){
        String url1="https://www.happybi.com.tw/api/productList";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url1+"?page="+page,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonArray=new JSONArray();
                        try {
                            jsonArray=response.getJSONArray("productList");
                            hasNextPage=response.getBoolean("hasNextPage");
                        }catch (JSONException e){ }

                        list= jasonList_2_objList.convert_2_Product_list(context,jsonArray);

                        market_listview_adapter =new market_listview_adapter(context,list);

                        market_listView.setAdapter(market_listview_adapter);
                        market_listView.setOnScrollListener(onScrollListener);
                        market_progressbar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    new ios_style_alert_dialog_1.Builder(context)
                            .setTitle("連線錯誤")
                            .setMessage("請重新登入")
                            .show();
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
    }
    private void get_myOrderList(){
        String url2="https://www.happybi.com.tw/api/order/myOrderList";
        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(0, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray=new JSONArray();
                try {
                    jsonArray=response.getJSONArray("orderList");
                    hasNextPage=response.getBoolean("hasNextPage");
                }catch (JSONException e){ }

                list2= jasonList_2_objList.convert_2_Order_list(context,jsonArray);
                market_myOrder_listview_adapter =new market_myOrder_listview_adapter(context,list2);
                market_listView.setAdapter(market_myOrder_listview_adapter);
                market_listView.setOnScrollListener(onScrollListener);
                market_progressbar.setVisibility(View.GONE);
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
    }
    private void load_more_product(){
        String url1="https://www.happybi.com.tw/api/productList";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url1+"?page="+page,null,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray=new JSONArray();
                try {
                    jsonArray=response.getJSONArray("productList");
                    hasNextPage=response.getBoolean("hasNextPage");
                }catch (JSONException e){ }
                List<Product_class> t=new ArrayList<Product_class>();

                t= jasonList_2_objList.convert_2_Product_list(context,jsonArray);
                list.addAll(t);

                market_listview_adapter.notifyDataSetChanged();
//                market_listView.setAdapter(market_listview_adapter);
                market_listView.setOnScrollListener(onScrollListener);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
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
    }
    private void load_more_myproduct(){
        String url2="https://www.happybi.com.tw/api/order/myOrderList";
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
    }

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
                        load_more_product();

                        market_listView.setOnScrollListener(null);
                    }else{
                        page++;
                        load_more_myproduct();

                        market_listView.setOnScrollListener(null);
                    }

                }
            }

        };
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
                    FT.hide(fragment2);
                } else {
                    FT.add(R.id._fragment_frag1_blank, fragment, "product_detail_Frag");
                    FT.hide(fragment2);
                }
            } else{
                FT.add(R.id._fragment_frag1_blank, new product_detail_Frag(product_class), "product_detail_Frag");
                FT.hide(fragment2);
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
                    market_progressbar.setVisibility(View.VISIBLE);
                    btn_flag="left";
                    page=1;
                    market_product.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_myProduct.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(click_listener);
                    market_product.setOnClickListener(null);
                    get_productList();
                    break;

                case R.id._market_myProduct:
                    market_progressbar.setVisibility(View.VISIBLE);

                    btn_flag="right";
                    page=1;
                    market_myProduct.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_product.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_product.setOnClickListener(button_listener);
                    market_listView.setOnItemClickListener(null);
                    market_myProduct.setOnClickListener(null);
                    get_myOrderList();
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
