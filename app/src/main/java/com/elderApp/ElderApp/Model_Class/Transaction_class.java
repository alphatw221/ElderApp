package com.elderApp.ElderApp.Model_Class;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction_class {
    public int id,user_id,amount,give_take;
    public String tran_id,event,target_id,created_at,update_at,target_name;


    public static Transaction_class getInstance(JSONObject jsonObject){
        Transaction_class object = new Transaction_class();
        try{
            object.id = jsonObject.getInt("id");
            object.tran_id = jsonObject.getString("tran_id");
            object.user_id = jsonObject.getInt("user_id");
            object.event = jsonObject.getString("event");
            object.amount = jsonObject.getInt("amount");
            object.give_take = jsonObject.getInt("give_take");
            object.target_id = jsonObject.getString("target_id");
            object.created_at = jsonObject.getString("created_at");
            object.update_at = jsonObject.getString("updated_at");
            object.target_name = jsonObject.getString("target_name");
        }catch (JSONException e) {
            return null;
        }
        return object;
    }
}
