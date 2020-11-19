package com.elderApp.ElderApp.Helper_Class;

import android.app.AlertDialog;
import android.content.Context;
import android.util.EventLog;

import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.Model_Class.LocationQuantity_class;
import com.elderApp.ElderApp.Model_Class.Location_class;
import com.elderApp.ElderApp.Model_Class.Order_class;
import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.Model_Class.Transaction_class;
import com.elderApp.ElderApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class jasonList_2_objList {
    private static Context context;

    public jasonList_2_objList(){}

    public jasonList_2_objList(Context c){
        context=c;
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List<Event_class> eventList(JSONArray jsonArray){
        List<Event_class> list = new ArrayList();
        for (int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(Event_class.getInstance(jsonObject));
            }catch (JSONException e){
                return null;
            }
        }
        return list;
    }


    //----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List<Transaction_class> convert_2_transaction_list(Context context,JSONArray jsonArray){
        List<Transaction_class> list=new ArrayList();
        for (int i=0;i<jsonArray.length();i++){
            try{
                list.add(convert_2_transaction(jsonArray.getJSONObject(i)));
            }catch (JSONException e){
                new AlertDialog.Builder(context)
                        .setTitle("錯誤")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(e.toString())
                        .show();
            }
        }

        return list;
    };

    static private Transaction_class convert_2_transaction(JSONObject object){
        Transaction_class transaction_class=new Transaction_class();
        try{
            transaction_class.id=object.getInt("id");
            transaction_class.tran_id=object.getString("tran_id");
            transaction_class.user_id=object.getInt("user_id");
            transaction_class.event=object.getString("event");
            transaction_class.amount=object.getInt("amount");
            transaction_class.give_take=object.getInt("give_take");
            transaction_class.target_id=object.getString("target_id");
            transaction_class.created_at=object.getString("created_at");
            transaction_class.update_at=object.getString("updated_at");
            transaction_class.target_name=object.getString("target_name");
        }catch (JSONException e)
        {
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return transaction_class;
    };

    public static List<Transaction_class> transactionList(JSONArray jsonArray){
        List<Transaction_class> list=new ArrayList();
        for (int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(Transaction_class.getInstance(jsonObject));
            }catch (JSONException e){
                return null;
            }
        }
        return list;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List convert_2_Product_list(Context context,JSONArray jsonArray){

        List<Product_class> list=new ArrayList();
//        JSONArray jsonArray_products,jsonArray_cats=new JSONArray();
        try{

            for (int i=0;i<jsonArray.length();i++){
                list.add(convert_2_product(jsonArray.getJSONObject(i)));

            }
        }catch (JSONException e){

        }

        return list;
    }
    static private Product_class convert_2_product(JSONObject object){
        Product_class product_class=new Product_class();
        try{
            product_class.imgUrl=object.getString("imgUrl");
//            product_class.product_category_id=object.getInt("product_category_id");
            product_class.name=object.getString("name");
            product_class.price=object.getInt("price");
//            product_class.info=object.getString("info");
            product_class.pay_cash_price=object.getInt("pay_cash_price");
            product_class.slug=object.getString("slug");
//            product_class._public=object.getInt("public");
        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return product_class;
    }


    //----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List convert_2_Location_list(Context context,JSONArray jsonArray)
    {
        List<Location_class> list=new ArrayList();
        try{

            for (int i=0;i<jsonArray.length();i++){
                list.add(convert_2_loccation(jsonArray.getJSONObject(i)));

            }
        }catch (JSONException e){

        }

        return list;
    }
    private static Location_class convert_2_loccation(JSONObject object)
    {
        Location_class location_class=new Location_class();
        try{
            location_class.slug=object.getString("slug");
            location_class.user_id=object.getInt("user_id");
            location_class.id=object.getInt("id");
            location_class.name=object.getString("name");
            location_class.img=object.getString("img");
            location_class.address=object.getString("address");
            location_class.link=object.getString("link");
        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return location_class;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List convert_2_LocationQuantity_list(Context context,JSONArray jsonArray)
    {
        List<LocationQuantity_class> list=new ArrayList();
        try{

            for (int i=0;i<jsonArray.length();i++){
                list.add(convert_2_LocationQuantity(jsonArray.getJSONObject(i)));

            }
        }catch (JSONException e){

        }

        return list;
    }
    private static LocationQuantity_class convert_2_LocationQuantity(JSONObject object)
    {
        LocationQuantity_class locationquantity_class=new LocationQuantity_class();
        try{
            locationquantity_class.product_id=object.getInt("product_id");
            locationquantity_class.location_id=object.getInt("location_id");
            locationquantity_class.quantity=object.getInt("quantity");
        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return locationquantity_class;
    }


//----------------------------------------------------------------------------------------------------------------------------------------------------

    public static List convert_2_Order_list(Context context,JSONArray jsonArray)
    {
        List<Order_class> list=new ArrayList();
        try{

            for (int i=0;i<jsonArray.length();i++){
                list.add(convert_2_order(jsonArray.getJSONObject(i)));

            }
        }catch (JSONException e){

        }

        return list;
    }
    private static Order_class convert_2_order(JSONObject object)
    {
        Order_class order_class=new Order_class();
        try{
            order_class.address=object.getString("address");
            order_class.created_at=object.getString("created_at");
            order_class.imgUrl=object.getString("imgUrl");
//            order_class.location=object.getString("location");
            order_class.location_name=object.getString("location_name");
            order_class.name=object.getString("name");
//            order_class.product=object.getString("product");
            order_class.receive=object.getInt("receive");
//            order_class.updated_at=object.getString("updated_at");

        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return order_class;
    }

}