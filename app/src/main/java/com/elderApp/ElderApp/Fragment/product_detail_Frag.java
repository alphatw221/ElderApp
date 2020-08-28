package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.AlertDialog.LocationDetailAlertDialog;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.location_listview_adapter;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;
import com.elderApp.ElderApp.Model_Class.LocationQuantity_class;
import com.elderApp.ElderApp.Model_Class.Location_class;
import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    List<String> location_name_list=new ArrayList<>();
    List<String> location_slug_list=new ArrayList<>();
    List<Integer> location_id_list=new ArrayList<>();
    private int select_location_position;
    private WebView product_detail_webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        product_detail_webview=view.findViewById(R.id._product_detail_webview);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------

        product_detail_back.setOnClickListener(backListener);
        product_detail_listview.setOnItemClickListener(click_listener);
        product_detail_spinner.setOnItemSelectedListener(spinnerListener);


        //---------------------發出請求------------------------------------------------------------
        getProductDetail();
        //----------------------------------------------------------------------------------------------------------------------------------------------------


        return view;
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------

    private void getProductDetail(){
        String url="https://www.happybi.com.tw/api/product/productDetail/"+product_class.slug;

        //---------------------回報Listener1------------------------------------------------------------
        Response.Listener RL1=new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                JSONObject product=new JSONObject();
                JSONArray locationList=new JSONArray();
                try {
                    product=response.getJSONObject("product");
                    locationList=response.getJSONArray("locationList");
                }catch (JSONException e){
                    Log.d("error",e.toString());
                }
                location_listview_adapter location_listview_adapter=new location_listview_adapter(context,locationList);
                product_detail_listview.setAdapter(location_listview_adapter);
                setListViewHeightBasedOnChildren(product_detail_listview);
                try {
                    for(int i=0;i<locationList.length();i++){

                            location_name_list.add(locationList.getJSONObject(i).getString("name"));
                            location_slug_list.add(locationList.getJSONObject(i).getString("slug"));
                            location_id_list.add(locationList.getJSONObject(i).getInt("location_id"));


                    }

                    String url=product.getString("imgUrl");
                    Picasso.get().load(url).into(product_detail_image);
                    product_detail_name.setText("商品："+product.getString("name"));
                    product_detail_price.setText("樂幣："+product.getString("price"));
//                    product_detail_info.setText("商品資訊:\n\n"+product.getString("info"));
                    product_detail_info.setText("商品資訊:\n");
                    product_detail_webview.loadData(product.getString("info"),"text/html","UTF-8");
                }catch (JSONException e){

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,location_name_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                product_detail_spinner.setAdapter(adapter);
                product_detail_exchange.setOnClickListener(exchangeListener);
//
                //---------------------載圖片------------------------------------------------------------


            }
        };
        //---------------------錯誤回報Listener1------------------------------------------------------------
        Response.ErrorListener REL1=new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        };
        myJsonRequest.GET_Request.getJSON_object(url,null,null,context,RL1,REL1);


    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------






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

            select_location_position=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //--------------------------------------------返回Listener--------------------------------------------------------
    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {


            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("market_Frag");
            Fragment fragment2=FM.findFragmentByTag("product_detail_Frag");
//            if ( fragment!=null) {
//                if ( fragment.isAdded()) {
//                    FT.show(fragment);
//                    FT.remove(fragment2);
//                } else {
////                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
//                    FT.add(R.id._fragment_frag1_blank, fragment, "market_Frag");
//                    FT.remove(fragment2);
//                }
//            } else{
//                FT.replace(R.id._fragment_frag1_blank,new market_Frag(),"market_Frag");
//
//            }
            FT.replace(R.id._fragment_frag1_blank,new market_Frag(),"market_Frag");

            FT.commit();
        }
    };


    //--------------------------------------------確認兌換Listener--------------------------------------------------------
    private Button.OnClickListener exchangeListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
           new AlertDialog.Builder(getActivity()).setTitle("確定兌換").setMessage("兌換地點:"+location_name_list.get(select_location_position)).setPositiveButton("是", new DialogInterface.OnClickListener() {
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
                           String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                           Map<String, String> body = new HashMap<String, String>();
                           body.put("token", Token);
                           body.put("location_id", location_id_list.get(select_location_position).toString());
                           return body;
                       }
                   };
                   MySingleton.getInstance(context).getRequestQueue().add(stringRequest);

               }
           }).setNegativeButton("否",null).show();
        }
    };


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

