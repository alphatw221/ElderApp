package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.R;


public class frag1_blank extends Fragment {
    private FrameLayout f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag1_blank, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        f = (FrameLayout) view.findViewById(R.id._frag_blank);
        getFragmentManager().beginTransaction().add(R.id._fragment_frag1_blank,new Frag1(),"Frag1").commit();

        return view;
    }
}