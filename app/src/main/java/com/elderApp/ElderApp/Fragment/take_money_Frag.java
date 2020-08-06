package com.elderApp.ElderApp.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.elderApp.ElderApp.Helper_Class.QRCodeHelper;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class take_money_Frag extends Fragment {
    private ImageView take_money_qrcode;
    private Context context;
    private ImageButton take_money_back;
    private User user;

    public take_money_Frag(User user) {
        this.user=user;

    }

    public void setUser(User u){user=u;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=this.getContext();
        View view= inflater.inflate(R.layout.frag_take_money_,container,false);
        take_money_qrcode=(ImageView)view.findViewById(R.id._take_money_qrcode);


        String text=user.user_id+","+user.name+","+user.email;
        Bitmap bitmap = QRCodeHelper
            .newInstance(context)
            .setContent(text)
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
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("Frag1");
            Fragment fragment2=FM.findFragmentByTag("take_money_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "take_money_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

            }
//            if ( fragment2!=null) {
//                if ( fragment2.isAdded()) {
//                    FT.remove(fragment2);
//                }
//            }
            FT.commit();
        }
    };


}
