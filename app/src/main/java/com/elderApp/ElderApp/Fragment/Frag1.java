package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Frag1 extends Fragment {
    private TextView person_name,person_rank,person_happybi,person_org_rank,home_title;
    private ImageButton takeBi,giveBi,myTrans,exchange,web_market;
    private Button person_org_rank_btn;
    private ConstraintLayout person_org_rank_layout;
    private WebView webView;
    private Context context;
    private User user;
    private FrameLayout frameLayout;
    public ScrollView frag1_base;
    public String Token;
    public Frag1() {          //Frag1建構子

        this.user= TabActivity.user;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag1_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
        person_name=(TextView) view.findViewById(R.id._person_name);
        person_rank=(TextView) view.findViewById(R.id._person_rank);
        person_happybi=(TextView) view.findViewById(R.id._person_happybi);
        person_org_rank=(TextView)view.findViewById(R.id._person_org_rank);
        person_org_rank_layout=view.findViewById(R.id._person_org_rank_layout);
        person_org_rank_btn=view.findViewById(R.id._person_org_rank_btn);
        takeBi=(ImageButton)view.findViewById(R.id._takeBi);
        giveBi=(ImageButton)view.findViewById(R.id._giveBi);
        myTrans=(ImageButton)view.findViewById(R.id._myTrans);
        exchange=(ImageButton)view.findViewById(R.id._exchange);
        webView=(WebView)view.findViewById(R.id._web_view);
        frag1_base=(ScrollView)view.findViewById(R.id._frag1_base);
        frameLayout=view.findViewById(R.id._frag1_fragment);
        web_market=view.findViewById(R.id._web_market);
        context=this.getContext();
        //---------------------發出請求------------------------------------------------------------
        user_me_request();
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        takeBi.setOnClickListener(takeBi_listener);
        giveBi.setOnClickListener(giveBi_listener);
        myTrans.setOnClickListener(myTrans_listener);
        exchange.setOnClickListener(exchange_listener);
        web_market.setOnClickListener(web_market_listener);

        webView.loadUrl("https://www.happybi.com.tw/slider.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());


        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }


    //-----------------收樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener takeBi_listener =new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
//            getFragmentManager().beginTransaction().replace(R.id._frag1_fragment,new take_money_Frag(user),"take_money_Frag").commit();
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("take_money_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag1");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "take_money_Frag");
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new take_money_Frag(user),"take_money_Frag");

            }
            FT.commit();
        }

        ;
    };
    //-----------------送樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener giveBi_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("give_money_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag1");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);

                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "give_money_Frag");

                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new give_money_Frag(),"give_money_Frag");
            }
            FT.commit();
        }
    };

    //-----------------我的帳簿按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener myTrans_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("my_transaction_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag1");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);

                } else {
                    FT.add(R.id._fragment_frag1_blank, fragment, "my_transaction_Frag");

                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new my_transaction_Frag(),"my_transaction_Frag");
            }
            FT.commit();
        }
    };
    //-----------------商品兌換按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener exchange_listener =new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("market_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag1");
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
    //-----------------網路商城按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener web_market_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("webview_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag1");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.hide(fragment2);

                } else {
                    FT.add(R.id._fragment_frag1_blank, fragment, "webview_Frag");
                    FT.hide(fragment2);
                }
            } else{
                String url="https://www.happybi.com.tw/product/list?token="+Token;
                FT.add(R.id._fragment_frag1_blank,new webview_Frag(url),"webview_Frag");
                FT.hide(fragment2);
            }
            FT.commit();
        }
    };


    public void setUser(User u){
        this.user=u;
    }
    //----------------------------------------------------------------------------------------------------------------------------------------
    private void user_me_request(){
        String url="https://www.happybi.com.tw/api/auth/me";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    person_name.setText("姓名:"+response.getString("name"));
                    person_happybi.setText("剩餘樂幣:"+response.getString("wallet"));
                    person_rank.setText("榮譽等級:"+Integer.toString(response.getInt("rank")));
                    int i=response.optInt("org_rank");
                    if(i>0){
                        if(response.getInt("org_rank")==1){
                            person_org_rank_layout.setVisibility(View.GONE);
                        }else{
                            String[] s={"","平民","小天使","大天使","守護天使","領航天使"};
                            person_org_rank.setText("職務:"+s[response.getInt("org_rank")]);
                            person_org_rank_btn.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent i =new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("https://www.happybi.com.tw/memberGroupMembers?token="+Token));
                                    context.startActivity(i);

                                }
                            });
                        }
                    }else{
                        person_org_rank_layout.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    Log.d("json error",e.toString());
                }
            }
        }, new Response.ErrorListener() {
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
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };
        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }


}
