package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class jasonList_2_objList {
    private static Context context;

    public jasonList_2_objList(){}

    public jasonList_2_objList(Context c){
        context=c;
    }

    public static List convert_2_Event_list(Context context,JSONArray jsonArray){

        List<Event_class> list=new ArrayList();
        for (int i=0;i<jsonArray.length();i++){
            try{
                list.add(convert_2_event(jsonArray.getJSONObject(i)));
            }catch (JSONException e){
                new AlertDialog.Builder(context)
                        .setTitle("錯誤")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(e.toString())
                        .show();
            }
        }
        return list;
    }
    public static List convert_2_transaction_list(Context context,JSONArray jsonArray){
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

    static private Event_class convert_2_event(JSONObject object){
        Event_class event_class=new Event_class();
        try{

            event_class.id=object.getInt("id");
            event_class.category_id=object.getInt("category_id");
            event_class.district_id=object.getInt("district_id");
            event_class.reward_level_id=object.getInt("reward_level_id");
            event_class.title=object.getString("title");
            event_class.body=object.getString("body");
            event_class.dateTime=object.getString("dateTime");
            event_class.dateTime_2=object.getString("dateTime_2");
            event_class.location=object.getString("location");
            event_class.image=object.getString("image");
            event_class.deadline=object.getString("deadline");
            event_class.created_at=object.getString("created_at");
            event_class.updated_at=object.getString("updated_at");
            event_class.slug=object.getString("slug");
            event_class.maximum=object.getInt("maximum");
            event_class.people=object.getInt("people");
            event_class.numberOfPeople=object.getInt("numberOfPeople");
        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return event_class;
    }

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


}