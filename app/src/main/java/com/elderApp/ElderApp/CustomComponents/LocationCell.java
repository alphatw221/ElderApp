package com.elderApp.ElderApp.CustomComponents;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.elderApp.ElderApp.Helper_Class.ProductDetailDelegate;
import com.elderApp.ElderApp.R;

public class LocationCell extends ConstraintLayout {

    public ProductDetailDelegate delegate;

    private String name;
    private String address;
    private String link;
    private int quantity;
    public int location_id;

    private TextView location_name;
    private Button detail_button;
    private ImageView checkbox_image;
    public boolean isChecked = false;
    public void setChecked(boolean checked) {
        isChecked = checked;
        if(checked){
            checkbox_image.setImageResource(R.drawable.shape_radio_button_location_selected);
            delegate.selectLocation(location_id);
        }else{
            checkbox_image.setImageResource(R.drawable.shape_rectangle_radio_button);
        }
    }

    public LocationCell(Context context, int location_id, String name, String address, String link, int quantity) {
        super(context);
        this.location_id = location_id;
        this.name = name;
        this.address = address;
        this.link = link;
        this.quantity = quantity;
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.location_cell,this);
        checkbox_image = findViewById(R.id.checkbox_image);
        checkbox_image.setOnClickListener(click_checkbox);
        detail_button = findViewById(R.id.detail_button);
        detail_button.setOnClickListener(click_detail_button);
        location_name = findViewById(R.id.location_name);
        location_name.setText(this.name+"(數量:"+ this.quantity +")");

    }

    private Button.OnClickListener click_detail_button = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            delegate.showLocationDetail(name,address,link);
        }
    };

    private ImageView.OnClickListener click_checkbox = new ImageView.OnClickListener(){
        @Override
        public void onClick(View v) {
            setChecked(!isChecked);
        }
    };



}
