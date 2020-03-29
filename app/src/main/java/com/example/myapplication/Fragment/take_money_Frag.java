package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.Helper_Class.QRCodeHelper;
import com.example.myapplication.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class take_money_Frag extends Fragment {
    private ImageView take_money_qrcode;
    private Context context;
    private ImageButton take_money_back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=this.getContext();
        View view= inflater.inflate(R.layout.frag_take_money_,container,false);
        take_money_qrcode=(ImageView)view.findViewById(R.id._take_money_qrcode);
        String string="I love you";
        Bitmap bitmap = QRCodeHelper
            .newInstance(context)
            .setContent(string)
            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
            .setMargin(2)
            .getQRCOde();
        take_money_qrcode.setImageBitmap(bitmap);
        take_money_back=view.findViewById(R.id._take_money_back);
        take_money_back.setOnClickListener(backListener);
        return view;
    }
    private ImageButton.OnClickListener backListener=new ImageButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("frag1") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("frag1")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new Frag1(), "frag1").commit();
            }
            if(fragmentManager.findFragmentByTag("take_money") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("take_money")).commit();
            }
        }
    };


}
