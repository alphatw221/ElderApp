package com.example.myapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;


public class product_detail_Frag extends Fragment {

    private Product_class product_class;
    public product_detail_Frag(Product_class pc){
        product_class=pc;
    }
    private ImageButton product_detail_back;
    private Button product_detail_exchange;
    private TextView product_detail_name,product_detail_price,product_detail_info;
    private ImageView product_detail_image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_detail, container, false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------

        product_detail_name=(TextView)view.findViewById(R.id._product_detail_name);
        product_detail_price=(TextView)view.findViewById(R.id._product_detail_price);
        product_detail_info=(TextView)view.findViewById(R.id._product_detail_info);
        product_detail_image=(ImageView)view.findViewById(R.id._product_detail_image);
        product_detail_back=(ImageButton)view.findViewById(R.id._product_detail_back);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        product_detail_name.setText(product_class.name);
        product_detail_price.setText(Integer.toString(product_class.price));
        product_detail_info.setText(product_class.info);
        product_detail_back.setOnClickListener(backListener);

        String url="https://www.happybi.com.tw/images/products/"+product_class.slug+"/"+product_class.img;
        Picasso.get().load(url).into(product_detail_image);
        //---------------------發出請求------------------------------------------------------------
        //----------------------------------------------------------------------------------------------------------------------------------------------------


        return view;
    }

    //--------------------------------------------返回Listener--------------------------------------------------------
    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.home_fragment_container,new take_money_Frag()).commit();
            if(fragmentManager.findFragmentByTag("market") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("market")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, new market_Frag(), "market").commit();
            }
            if(fragmentManager.findFragmentByTag("product_detail") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("product_detail")).commit();
//                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("give_money")).commit();
            }
        }
    };
}

