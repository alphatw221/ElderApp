package com.example.myapplication.Fragment;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class give_money_Frag extends Fragment implements ZXingScannerView.ResultHandler {

    private ImageButton backButton;
    private ZXingScannerView qrScanner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_give_money_, container, false);
        qrScanner=view.findViewById(R.id._qrCodeScanner);
        backButton = view.findViewById(R.id._give_money_back);
        backButton.setOnClickListener(backListener);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrScanner.stopCamera();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        qrScanner.stopCamera();
    }

    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

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
            if(fragmentManager.findFragmentByTag("give_money") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("give_money")).commit();
//                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("give_money")).commit();
            }
        }
    };

    private PermissionListener permissionListener=new PermissionListener() {
        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
            qrScanner.setResultHandler(give_money_Frag.this);
            qrScanner.startCamera();
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {

        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

        }
    };



    @Override
    public void handleResult(Result rawResult) {

    }
}