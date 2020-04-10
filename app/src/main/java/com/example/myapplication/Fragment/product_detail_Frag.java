package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.AlertDialog.LocationDetailAlertDialog;
import com.example.myapplication.Helper_Class.MySingleton;
import com.example.myapplication.Helper_Class.jasonList_2_objList;
import com.example.myapplication.Helper_Class.location_listview_adapter;
import com.example.myapplication.Helper_Class.market_listview_adapter;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.example.myapplication.Model_Class.LocationQuantity_class;
import com.example.myapplication.Model_Class.Location_class;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class product_detail_Frag extends Fragment {

    private Product_class product_class;
    public product_detail_Frag(Product_class pc){
        product_class=pc;
    }
    private ImageButton product_detail_back;
    private Button product_detail_exchange;
    private TextView product_detail_name,product_detail_price,product_detail_info;
    private ImageView product_detail_image;
    private ListView product_detail_listview;
    private Spinner product_detail_spinner;
    private Context context;
    private List<Location_class> location_list=new ArrayList<>();
    private List<LocationQuantity_class> locationquantity_list;
    private Map<Integer,Integer> location_Dic=new HashMap<Integer, Integer>();
    private List<String> location_string_list=new ArrayList<String>();
    private String Token;
    private Location_class select_location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Token=this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_detail, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        product_detail_name=(TextView)view.findViewById(R.id._product_detail_name);
        product_detail_price=(TextView)view.findViewById(R.id._product_detail_price);
        product_detail_info=(TextView)view.findViewById(R.id._product_detail_info);
        product_detail_image=(ImageView)view.findViewById(R.id._product_detail_image);
        product_detail_back=(ImageButton)view.findViewById(R.id._product_detail_back);
        product_detail_listview=(ListView)view.findViewById(R.id._product_detail_listview);
        product_detail_spinner=(Spinner)view.findViewById(R.id._product_detail_spinner);
        product_detail_exchange=(Button)view.findViewById(R.id._product_detail_exchange);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        product_detail_name.setText("商品："+product_class.name);
        product_detail_price.setText("樂幣："+Integer.toString(product_class.price));
        product_detail_info.setText("商品資訊:\n\n"+product_class.info);
        product_detail_back.setOnClickListener(backListener);
        product_detail_listview.setOnItemClickListener(click_listener);
        product_detail_spinner.setOnItemSelectedListener(spinnerListener);

        //---------------------載圖片------------------------------------------------------------
        String url="https://www.happybi.com.tw/images/products/"+product_class.slug+"/"+product_class.img;
        Picasso.get().load(url).into(product_detail_image);
        //---------------------發出請求------------------------------------------------------------
        getLocationAndQuantity();
        //----------------------------------------------------------------------------------------------------------------------------------------------------


        return view;
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------

    private void getLocationAndQuantity(){
        String url2="https://www.happybi.com.tw/api/getLocationAndQuantity/"+product_class.slug;

        //---------------------回報Listener1------------------------------------------------------------
        Response.Listener RL1=new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                locationquantity_list=jasonList_2_objList.convert_2_LocationQuantity_list(context,response);
                for(LocationQuantity_class lq:locationquantity_list){
                    location_Dic.put(lq.location_id,lq.quantity);
                }
                getAllLocation();

            }
        };
        //---------------------錯誤回報Listener1------------------------------------------------------------
        Response.ErrorListener REL1=new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("錯誤")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(error.toString())
                        .show();
            }
        };
        myJsonRequest.GET_Request.getJSON_array(url2,null,null,getActivity().getApplicationContext(),RL1,REL1);


    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------

    private void getAllLocation(){
        String url3="https://www.happybi.com.tw/api/location";

        //---------------------回報Listener2------------------------------------------------------------
        Response.Listener RL2=new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                List<Location_class> All_location_list=jasonList_2_objList.convert_2_Location_list(context,response);
                for(Location_class lc:All_location_list){
                    if(location_Dic.containsKey(lc.id)){
                        location_list.add(lc);
                        location_string_list.add(lc.name);
                    }
                }
                location_listview_adapter location_listview_adapter=new location_listview_adapter(context,location_list,location_Dic);
                product_detail_listview.setAdapter(location_listview_adapter);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,location_string_list.toArray(new String[0]));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                product_detail_spinner.setAdapter(adapter);
                product_detail_exchange.setOnClickListener(exchangeListener);
            }
        };
        //---------------------錯誤回報Listener2------------------------------------------------------------
        Response.ErrorListener REL2=new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("錯誤")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(error.toString())
                        .show();
            }
        };

        myJsonRequest.GET_Request.getJSON_array(url3,null,null,getActivity().getApplicationContext(),RL2,REL2);

    }




    //---------------------------------------listview_click Listener---------------------------------------------------

    private ListView.OnItemClickListener click_listener= new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Object object=parent.getItemAtPosition(position);
            Location_class location_class=(Location_class)object;

            new LocationDetailAlertDialog(context,location_class).show();



        }

    };
    //-------------------------------地點下拉Listener----------------------------------------------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener spinnerListener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            select_location=location_list.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //--------------------------------------------返回Listener--------------------------------------------------------
    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("market") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("market")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new market_Frag(), "market").commit();
            }
            if(fragmentManager.findFragmentByTag("product_detail") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("product_detail")).commit();
//                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("give_money")).commit();
            }
        }
    };


    //--------------------------------------------確認兌換Listener--------------------------------------------------------
    private Button.OnClickListener exchangeListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
           new AlertDialog.Builder(getActivity()).setTitle("確定兌換").setMessage("兌換地點:"+select_location.name).setPositiveButton("是", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.happybi.com.tw/api/purchase/"+product_class.slug, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           if(response.equals("success")){
                               new AlertDialog.Builder(getActivity()).setMessage("兌換成功!").show();
                           }else{
                               new AlertDialog.Builder(getActivity()).setMessage(response).show();
                           }

                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           try {
                               String body = new String(error.networkResponse.data,"UTF-8");
                               new AlertDialog.Builder(getActivity()).setMessage(body).show();
                           } catch (UnsupportedEncodingException e) {
                               e.printStackTrace();
                           }

                       }
                   }){
                       @Override
                       protected Map<String, String> getParams() throws AuthFailureError {
                           Map<String, String> body = new HashMap<String, String>();
                           body.put("token", Token);
                           body.put("location_id", Integer.toString(select_location.id));
                           return body;
                       }
                   };
                   MySingleton.getInstance(context).getRequestQueue().add(stringRequest);

               }
           }).setNegativeButton("否",null).show();
        }
    };
}

