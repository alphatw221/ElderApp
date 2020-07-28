package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.R;

import java.util.List;


public class frag2_blank extends Fragment {

    private FrameLayout f;
    private List<Event_class> e;

    public frag2_blank(List<Event_class> e) {
        this.e = e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag2_blank, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        f = (FrameLayout) view.findViewById(R.id._frag_blank);
        getFragmentManager().beginTransaction().add(R.id._fragment_frag2_blank,new Frag2(e),"Frag2").commit();

        return view;
    }
}