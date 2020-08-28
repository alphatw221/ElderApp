package com.elderApp.ElderApp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.elderApp.ElderApp.R;



public class webview_Frag extends Fragment {
    private ImageButton webview_back;
    private WebView webview_webview;

    private String url;
    public webview_Frag(String url) {
        this.url=url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_webview_, container, false);
        webview_back=view.findViewById(R.id._webview_back);
        webview_webview=view.findViewById(R.id._webview_webview);
        webview_back.setOnClickListener(btn_listener);

        webview_webview.loadUrl(url);
        WebSettings webSettings = webview_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview_webview.setWebViewClient(new WebViewClient());

        return view;
    }

    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id._webview_back:
                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("Frag1");
                    Fragment fragment2=FM.findFragmentByTag("webview_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.remove(fragment2);

                        } else {
                            FT.add(R.id._fragment_frag1_blank, fragment, "Frag1");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

                    }
                    FT.commit();
                    break;
            }
        }
    };
}