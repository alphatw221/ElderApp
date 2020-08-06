package com.elderApp.ElderApp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.elderApp.ElderApp.R;


public class frag2_blank extends Fragment {

    private FrameLayout f;

    public frag2_blank() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag2_blank, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        f = (FrameLayout) view.findViewById(R.id._frag_blank);
        getFragmentManager().beginTransaction().add(R.id._fragment_frag2_blank,new Frag2(),"Frag2").commit();

        return view;
    }
}