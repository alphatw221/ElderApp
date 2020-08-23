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

public class frag4_blank extends Fragment {
    private FrameLayout f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag4_blank, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        f = (FrameLayout) view.findViewById(R.id._frag_blank);
        getFragmentManager().beginTransaction().add(R.id._fragment_frag4_blank, new Frag4(), "Frag4").commit();

        return view;
    }
}