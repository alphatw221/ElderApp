package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.LoginActivity;
import com.elderApp.ElderApp.Activity.MainActivity;
import com.elderApp.ElderApp.Activity.MarketActivity;
import com.elderApp.ElderApp.Activity.MyTransactionActivity;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.Activity.TakeMoneyActivity;
import com.elderApp.ElderApp.Activity.WebViewActivity;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.zone.ZoneRules;
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
    private FrameLayout frameLayout;
    public ScrollView frag1_base;

    public Frag1() {          //Frag1建構子

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag1_layout,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
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

        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        takeBi.setOnClickListener(takeBi_listener);
        giveBi.setOnClickListener(giveBi_listener);
        myTrans.setOnClickListener(myTrans_listener);
        exchange.setOnClickListener(exchange_listener);
        web_market.setOnClickListener(web_market_listener);

        webView.loadUrl(apiService.host + "/slider.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        //----------------------------------------------------------------------------------------------------------------------------------------------------

        LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver,new IntentFilter("update-user-info"));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("message receive !");
            updateUserInfo();
        }
    };

    //-----------------收樂幣按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener takeBi_listener =new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("click take money button");

            Intent intent = new Intent(context,TakeMoneyActivity.class);
            startActivity(intent);

        }

//        @Override
//        public void onClick(View v) {
//            FragmentManager FM = getFragmentManager();
//            FragmentTransaction FT = FM.beginTransaction();
//            Fragment fragment=FM.findFragmentByTag("take_money_Frag");
//            Fragment fragment2=FM.findFragmentByTag("Frag1");
//            if ( fragment!=null) {
//                if ( fragment.isAdded()) {
//                    FT.show(fragment);
//                } else {
//                    FT.add(R.id._fragment_frag1_blank, fragment, "take_money_Frag");
//                }
//            } else{
//                FT.replace(R.id._fragment_frag1_blank,new take_money_Frag(TabActivity.user),"take_money_Frag");
//
//            }
//            FT.commit();
//        }
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

            Intent intent = new Intent(context, MyTransactionActivity.class);
            startActivity(intent);

        }
    };
    //-----------------商品兌換按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener exchange_listener =new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, MarketActivity.class);
            startActivity(intent);

        }
    };
    //-----------------網路商城按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener web_market_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url",apiService.host + "/product/list?token=" + TabActivity.user.access_token);
            startActivity(intent);

        }
    };



    //----------------------------------------------------------------------------------------------------------------------------------------
    private void updateUserInfo(){

        person_name.setText("姓名:"+TabActivity.user.name);
        person_happybi.setText("剩餘樂幣:"+TabActivity.user.wallet);
        person_rank.setText("榮譽等級:"+Integer.toString(TabActivity.user.rank));


        if(TabActivity.user.org_rank > 2){
            person_org_rank_layout.setVisibility(View.VISIBLE);
            String[] s={"","平民","小天使","大天使","守護天使","領航天使"};
            person_org_rank.setText("職務:"+s[TabActivity.user.org_rank]);
            person_org_rank_btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url",apiService.host + "/memberGroupMembers?token=" + TabActivity.user.access_token);
                    startActivity(intent);
                }
            });
        }else{
            person_org_rank_layout.setVisibility(View.GONE);
        }

    }


}
