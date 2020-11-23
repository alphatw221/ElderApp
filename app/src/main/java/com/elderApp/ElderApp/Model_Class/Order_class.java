package com.elderApp.ElderApp.Model_Class;

import android.app.AlertDialog;

import com.elderApp.ElderApp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Order_class {

    public String location_name ;
    public int receive ;
    public String created_at ;
    public String updated_at ;
    public String product ;
    public String imgUrl ;
    public String location ;
    public String address ;
    public String name;



    public static Order_class getInstance(JSONObject jsonObject){
        Order_class object = new Order_class();
        try{
            object.address = jsonObject.getString("address");
            object.created_at = jsonObject.getString("created_at");
            object.imgUrl = jsonObject.getString("imgUrl");
            object.location_name = jsonObject.getString("location_name");
            object.name = jsonObject.getString("name");
            object.receive = jsonObject.getInt("receive");
        }catch (JSONException e){ }
        return object;
    }
}
