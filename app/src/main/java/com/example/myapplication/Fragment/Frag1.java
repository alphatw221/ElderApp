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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.Model_Class.User;
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
    private User user;
    private FrameLayout frameLayout;
    public ScrollView frag1_base;
    public Frag1() {          //Frag1建構子

        this.user= TabActivity.user;
    }


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
        frag1_base=(ScrollView)view.findViewById(R.id._frag1_base);
        frameLayout=view.findViewById(R.id._frag1_fragment);
        //---------------------發出請求------------------------------------------------------------
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        takeBi.setOnClickListener(takeBi_listener);
        giveBi.setOnClickListener(giveBi_listener);
        myTrans.setOnClickListener(myTrans_listener);
        exchange.setOnClickListener(exchange_listener);

        person_name.setText("姓名:"+user.name);
        person_happybi.setText("剩餘樂幣:"+user.wallet);
        person_rank.setText("榮譽等級:"+user.rank);

        webView.loadUrl("https://www.happybi.com.tw/slider.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        context=this.getContext();
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
                    FT.add(R.id._frag1_fragment, fragment, "take_money_Frag");
                }
            } else{
                FT.replace(R.id._frag1_fragment,new take_money_Frag(user),"take_money_Frag");

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
                    FT.add(R.id._frag1_fragment, fragment, "give_money_Frag");

                }
            } else{
                FT.replace(R.id._frag1_fragment,new give_money_Frag(),"give_money_Frag");
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
                    FT.add(R.id._frag1_fragment, fragment, "my_transaction_Frag");

                }
            } else{
                FT.replace(R.id._frag1_fragment,new my_transaction_Frag(),"my_transaction_Frag");
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

                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._frag1_fragment, fragment, "market_Frag");

                }
            } else{
                FT.replace(R.id._frag1_fragment,new market_Frag(),"market_Frag");
            }
            FT.commit();
        }
    };

    public void setUser(User u){
        this.user=u;
    }
}
