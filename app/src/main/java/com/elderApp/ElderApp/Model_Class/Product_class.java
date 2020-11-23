package com.elderApp.ElderApp.Model_Class;

import android.app.AlertDialog;

import com.elderApp.ElderApp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Product_class {


    public int id ,product_category_id, price ,_public, pay_cash_price,pay_cash_point;

    public String name,slug,img,imgUrl,info;


    public static Product_class getInstance(JSONObject jsonObject){
        Product_class object = new Product_class();
        try{
            object.imgUrl = jsonObject.getString("imgUrl");
            object.name = jsonObject.getString("name");
            object.price = jsonObject.getInt("price");
            object.pay_cash_price = jsonObject.getInt("pay_cash_price");
            object.slug = jsonObject.getString("slug");
        }catch (JSONException e){ }
        return object;
    }
}
