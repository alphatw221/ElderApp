package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.AlertDialog.LocationDetailAlertDialog;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
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
    private Button product_detail_exchange,product_detail_purchase;
    private TextView product_detail_name,product_detail_price,product_detail_info;
    private ImageView product_detail_image;
    private ListView product_detail_listview;
    private Spinner product_detail_spinner;
    private RadioGroup prodcut_detail_radio_group;
    private Context context;
    private List<Location_class> location_list=new ArrayList<>();
    private List<LocationQuantity_class> locationquantity_list;
    private Map<Integer,Integer> location_Dic=new HashMap<Integer, Integer>();
    private List<String> location_string_list=new ArrayList<String>();
    List<String> location_name_list=new ArrayList<>();
    List<String> location_slug_list=new ArrayList<>();
    List<Integer> location_id_list=new ArrayList<>();
    private String buynowUrl;
    private WebView product_detail_webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_detail, container, false);
        context=this.getContext();
        product_detail_name=(TextView)view.findViewById(R.id._product_detail_name);
        product_detail_price=(TextView)view.findViewById(R.id._product_detail_price);
        product_detail_info=(TextView)view.findViewById(R.id._product_detail_info);
        product_detail_image=(ImageView)view.findViewById(R.id._product_detail_image);
        product_detail_back=(ImageButton)view.findViewById(R.id._product_detail_back);
//        product_detail_listview=(ListView)view.findViewById(R.id._product_detail_listview);
//        product_detail_spinner=(Spinner)view.findViewById(R.id._product_detail_spinner);
        product_detail_exchange=(Button)view.findViewById(R.id._product_detail_exchange);
        product_detail_purchase=(Button)view.findViewById(R.id._product_detail_purchase);
        product_detail_webview=view.findViewById(R.id._product_detail_webview);
        prodcut_detail_radio_group=view.findViewById(R.id._product_detail_radio_group);

        product_detail_back.setOnClickListener(btn_listener);
        product_detail_purchase.setOnClickListener(btn_listener);
        product_detail_exchange.setOnClickListener(btn_listener);
        product_detail_listview.setOnItemClickListener(click_listener);

        getProductDetail();


        return view;
    }

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
                }catch (JSONException e){ }
                location_listview_adapter location_listview_adapter=new location_listview_adapter(context,locationList);
                product_detail_listview.setAdapter(location_listview_adapter);
                setListViewHeightBasedOnChildren(product_detail_listview);
                try {
                    for(int i=0;i<locationList.length();i++){
                        View v2=LayoutInflater.from(context).inflate(R.layout.radio_btn_location_layout,null);

                            location_name_list.add(locationList.getJSONObject(i).getString("name"));
                            location_slug_list.add(locationList.getJSONObject(i).getString("slug"));
                            location_id_list.add(locationList.getJSONObject(i).getInt("location_id"));
                        ((RadioButton)v2.findViewById(R.id._radio_btn_location)).setText(locationList.getJSONObject(i).getString("name"));
                        ((RadioButton)v2.findViewById(R.id._radio_btn_location)).setId(i);
                        prodcut_detail_radio_group.addView(v2);

                    }

                    String url=product.getString("imgUrl");
                    buynowUrl=product.getString("buynowUrl");
                    Picasso.get().load(url).into(product_detail_image);
                    product_detail_name.setText("商品："+product.getString("name"));
                    product_detail_price.setText("樂幣："+product.getString("price"));

                    product_detail_info.setText("商品資訊:\n");
                    product_detail_webview.loadData(product.getString("info"),"text/html","UTF-8");
                }catch (JSONException e){ }

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,location_name_list);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                product_detail_spinner.setAdapter(adapter);

//


            }
        };
        //---------------------錯誤回報Listener1------------------------------------------------------------
        Response.ErrorListener REL1=new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請從新登入")
                        .show();
            }
        };
        myJsonRequest.GET_Request.getJSON_object(url,null,null,context,RL1,REL1);
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
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._product_detail_back:
                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("market_Frag");
                    Fragment fragment2=FM.findFragmentByTag("product_detail_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.remove(fragment2);
                        } else {
                            FT.add(R.id._frag1_fragment, fragment, "market_Frag");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._frag1_fragment,new market_Frag(),"market_Frag");
                    }
                    FT.commit();
                    break;
                case R.id._product_detail_exchange:
                    if(prodcut_detail_radio_group.getCheckedRadioButtonId()!=-1){
                        int id=prodcut_detail_radio_group.getCheckedRadioButtonId();
                        new ios_style_alert_dialog_1.Builder(context)
                                .setTitle("確認兌換")
                                .setMessage("兌換地點:"+location_name_list.get(id))
                                .setPositiveButton("確定",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        exchange_product();
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .show();
                    }else{
                        new ios_style_alert_dialog_1.Builder(context)
                                .setMessage("未選擇兌換地點")
                                .show();
                    }
                    break;
                case R.id._product_detail_purchase:
                    FragmentManager FM2 = getFragmentManager();
                    FragmentTransaction FT2 = FM2.beginTransaction();
                    Fragment fragment3=FM2.findFragmentByTag("webview_Frag");
                    Fragment fragment4=FM2.findFragmentByTag("product_detail_Frag");
                    if ( fragment3!=null) {
                        if ( fragment3.isAdded()) {
                            FT2.show(fragment3);
                            FT2.hide(fragment4);

                        } else {
                            FT2.add(R.id._fragment_frag1_blank, fragment3, "webview_Frag");
                            FT2.hide(fragment4);
                        }
                    } else{
                        String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                        FT2.add(R.id._fragment_frag1_blank,new webview_Frag(buynowUrl+"?token="+Token),"webview_Frag");
                        FT2.hide(fragment4);
                    }
                    FT2.commit();
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
            Fragment fragment=FM.findFragmentByTag("market_Frag");
            Fragment fragment2=FM.findFragmentByTag("product_detail_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
                    FT.add(R.id._frag1_fragment, fragment, "market_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._frag1_fragment,new market_Frag(),"market_Frag");
            }
            FT.commit();
        }
    };


    //--------------------------------------------確認兌換Listener--------------------------------------------------------
    private Button.OnClickListener exchangeListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(prodcut_detail_radio_group.getCheckedRadioButtonId()!=-1){
                int id=prodcut_detail_radio_group.getCheckedRadioButtonId();
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("確認兌換")
                        .setMessage("兌換地點:"+location_name_list.get(id))
                        .setPositiveButton("確定",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exchange_product();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }else{
                new ios_style_alert_dialog_1.Builder(context)
                        .setMessage("未選擇兌換地點")
                        .show();
            }
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

    private void exchange_product(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.happybi.com.tw/api/purchase/"+product_class.slug, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           if(response.equals("success")){
                               new ios_style_alert_dialog_1.Builder(context).setMessage("兌換成功").show();
                           }else{
                               new ios_style_alert_dialog_1.Builder(context).setMessage(response).show();
                           }

                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           try {
                               String body = new String(error.networkResponse.data,"UTF-8");
                               new ios_style_alert_dialog_1.Builder(context).setMessage(body).show();
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
                           int id=prodcut_detail_radio_group.getCheckedRadioButtonId();
                           body.put("location_id", location_id_list.get(id).toString());
                           return body;
                       }
                   };
                   MySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }
}

