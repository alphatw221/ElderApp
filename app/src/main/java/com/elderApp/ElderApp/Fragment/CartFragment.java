package com.elderApp.ElderApp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.Activity.WebViewActivity;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.R;

import org.json.JSONObject;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends DialogFragment {

    /** 產品代號*/
    public String slug;
    /** 購買的據點id*/
    public Integer location_id;
    /** 要購買的數量*/
    private EditText quantityText;
    /** 台幣總額*/
    private TextView total_cash;
    /** 樂幣總額*/
    private TextView total_point;
    /** 剩餘樂幣TextView*/
    private TextView wallet_remain;
    /** 送出按鈕*/
    private Button submitButton;
    /** 剩餘樂幣*/
    private Integer wallet;
    /** 每單位台幣*/
    public Integer cash_per_product;
    /** 每單位樂幣*/
    public Integer point_per_product;

    public CartFragment() {
        // Required empty public constructor
    }


    public static CartFragment newInstance(String slug,Integer location_id,Integer cash_per_product,Integer point_per_product) {

        CartFragment fragment = new CartFragment();

        fragment.cash_per_product = cash_per_product;
        fragment.point_per_product = point_per_product;
        fragment.slug = slug;
        fragment.location_id = location_id;

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            cash_per_product = getArguments().getInt("cash_per_product");
//            point_per_product = getArguments().getInt("point_per_product");
//        }
//
        apiService.getMeRequest(getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                wallet = response.optInt("wallet");
                wallet_remain.setText("剩餘樂幣：" + wallet);
            }
        }, ErrorHandler.defaultListener(getContext()));



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Remove the default background
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Inflate the new view with margins and background
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        // Set up a click listener to dismiss the popup if they click outside of the background view
        v.findViewById(R.id.popup_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        quantityText = (EditText)v.findViewById(R.id.quantityText);
        total_cash = (TextView)v.findViewById(R.id.total_cash);
        total_point = (TextView)v.findViewById(R.id.total_point);
        wallet_remain = (TextView)v.findViewById(R.id.wallet_remain);
        submitButton = (Button)v.findViewById(R.id.submitButton);
        quantityText.addTextChangedListener(textWatcher);
        submitButton.setOnClickListener(submit);

        quantityText.setText(Integer.toString(1));
        total_cash.setText(Integer.toString(cash_per_product));
        total_point.setText(Integer.toString(point_per_product));



        return v;

    }


    /**數量變更*/
    private TextWatcher textWatcher= new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(String.valueOf(quantityText.getText()).equals("")){
                total_cash.setText(Integer.toString(0));
                total_point.setText(Integer.toString(0));
                return;
            }
            Integer quantity = Integer.parseInt(String.valueOf(quantityText.getText()));
            total_cash.setText(Integer.toString(cash_per_product * quantity));
            total_point.setText(Integer.toString(point_per_product * quantity));
        }
        @Override
        public void afterTextChanged(Editable editable) { }
    };

    /**
     * 確定送出事件
     */
    private Button.OnClickListener submit = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(String.valueOf(quantityText.getText()).equals("") || String.valueOf(quantityText.getText()).equals("0")){
                ErrorHandler.alert(getContext(),"訊息","請輸入數量");
                return;
            }

            submitButton.setVisibility(View.GONE);
            Integer quantity = Integer.parseInt(String.valueOf(quantityText.getText()));
            apiService.purchaseProductByCashRequest(getContext(),slug,location_id,quantity,responseListener,errorListener);

        }
    };

    /**
     * 購買response
     */
    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            //
            submitButton.setVisibility(View.VISIBLE);

            String order_numero = "";
            try {
                order_numero = response.getString("order_numero");
            }catch(Exception e){
                return;
            }

            Intent intent = new Intent(getContext(), WebViewActivity.class);
            intent.putExtra("url",apiService.host +"/order/detail/" + order_numero + "?token=" + TabActivity.user.access_token + "&noFooter=1");
            startActivity(intent);
            dismiss();

        }
    };

    /**
     * 購買error
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            //
            submitButton.setVisibility(View.VISIBLE);

            String msg = ErrorHandler.errorMessage(error);
            if(!msg.isEmpty()){
                ErrorHandler.alert(getContext(),"錯誤",msg);
                return;
            }
            ErrorHandler.alert(getContext());
        }
    };

}