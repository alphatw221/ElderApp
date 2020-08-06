package com.elderApp.ElderApp.Fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.elderApp.ElderApp.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class give_money_Frag extends Fragment implements ZXingScannerView.ResultHandler {

    private ImageButton backButton,give_money_restart_btn;
    private ZXingScannerView qrScanner;
    private ScrollView frag1_base;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_give_money_, container, false);
        qrScanner=view.findViewById(R.id._qrCodeScanner);
        backButton = view.findViewById(R.id._give_money_back);
        give_money_restart_btn=view.findViewById(R.id._give_money_restart_btn);
        backButton.setOnClickListener(btn_listener);
        give_money_restart_btn.setOnClickListener(btn_listener);
        context=this.getContext();

        return view;
    }




    @Override
    public void onResume() {
        super.onResume();
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
        qrScanner.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        qrScanner.stopCamera();


    }







    private ImageButton.OnClickListener btn_listener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
//            frag1_base.setVisibility(View.VISIBLE);
            switch (v.getId()){
                case R.id._give_money_back:


                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("Frag1");
                    Fragment fragment2=FM.findFragmentByTag("give_money_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.remove(fragment2);
                        } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                            FT.add(R.id._fragment_frag1_blank, fragment, "give_money_Frag");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

                    }
                    FT.commit();
//                    }

                    break;
                case R.id._give_money_restart_btn:
                    Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(permissionListener).check();
                    qrScanner.startCamera();
                    break;
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
            //跳出提示：--------------------------------------
            //
            //
            //
            //
            //-----------------------------------------------
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("Frag1");
            Fragment fragment2=FM.findFragmentByTag("give_money_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "give_money_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

            }
            FT.commit();
        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

        }
    };



    @Override
    public void handleResult(Result rawResult) {
//        String text=user.user_id+","+user.name+","+user.email;
        String[] a=rawResult.getText().split(",");
        getFragmentManager().beginTransaction().add(R.id._fragment_frag1_blank,new give_money_comfirm_Frag(a[0],a[1],a[2]),"give_money_comfirm_Frag").commit();
        Fragment fragment=getFragmentManager().findFragmentByTag("give_money_Frag");

        getFragmentManager().beginTransaction().remove(fragment).commit();

    }
}