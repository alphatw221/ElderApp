package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.elderApp.ElderApp.R;

public class WebViewActivity extends AppCompatActivity {

    private Context context;

    private ImageButton webview_back;
    private WebView webview_webview;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.fragment_webview_);

        webview_back = findViewById(R.id._webview_back);
        webview_webview = findViewById(R.id._webview_webview);


        webview_back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        url = getIntent().getStringExtra("url");
        webview_webview.loadUrl(url);
        WebSettings settings = webview_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview_webview.setWebViewClient(new WebViewClient());

    }
}