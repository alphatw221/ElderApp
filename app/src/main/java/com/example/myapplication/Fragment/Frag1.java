package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Frag1 extends Fragment {
    private TextView person_name,person_rank,person_happybi,home_title;
    private ImageButton takeBi,giveBi,myTrans,exchange;
    private WebView webView;
    private Context context;
    private String url="https://www.happybi.com.tw/api/auth/me";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag1_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        person_name=(TextView) view.findViewById(R.id._person_name);
        person_rank=(TextView) view.findViewById(R.id._person_rank);
        person_happybi=(TextView) view.findViewById(R.id._person_happybi);
        takeBi=(ImageButton)view.findViewById(R.id._takeBi);
        giveBi=(ImageButton)view.findViewById(R.id._giveBi);
        myTrans=(ImageButton)view.findViewById(R.id._myTrans);
        exchange=(ImageButton)view.findViewById(R.id._exchange);
        webView=(WebView)view.findViewById(R.id._web_view);
        //---------------------發出請求------------------------------------------------------------
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
//        new myJsonRequest(url,"post",key,value,getActivity().getApplicationContext(),RL,REL).Fire();
        myJsonRequest.POST_Request.getJSON_object(url,key,value,getActivity().getApplicationContext(),RL,REL);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        takeBi.setOnClickListener(takeBi_listener);
        giveBi.setOnClickListener(giveBi_listener);
        myTrans.setOnClickListener(myTrans_listener);
        exchange.setOnClickListener(exchange_listener);

        webView.loadUrl("https://www.happybi.com.tw/slider.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        context=this.getContext();
        //----------------------------------------------------------------------------------------------------------------------------------------------------


        return view;
    }

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
                try{
                    person_name.setText("姓名:"+response.getString("name"));
                    person_happybi.setText("剩餘樂幣:"+response.getString("wallet"));
                    person_rank.setText("榮譽等級:"+response.getString("rank"));
                }catch (JSONException e){

                }
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
    //-----------------收樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener takeBi_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("take_money") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("take_money")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new take_money_Frag(), "take_money").commit();
            }
            if(fragmentManager.findFragmentByTag("frag1") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("frag1")).commit();
            }
        }
    };

    //-----------------送樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener giveBi_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("give_money") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("give_money")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new give_money_Frag(), "give_money").commit();
            }
            if(fragmentManager.findFragmentByTag("frag1") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("frag1")).commit();
            }
        }
    };

    //-----------------我的帳簿按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener myTrans_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("myTrans") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("myTrans")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new my_transaction_Frag(), "myTrans").commit();
            }
            if(fragmentManager.findFragmentByTag("frag1") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("frag1")).commit();
            }
        }
    };
    //-----------------商品兌換按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener exchange_listener =new Button.OnClickListener(){
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
            if(fragmentManager.findFragmentByTag("frag1") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("frag1")).commit();
            }
        }
    };
}
