package com.elderApp.ElderApp.Model_Class;

import org.json.JSONObject;

public class Event_class{

    public int id,category_id,district_id,reward_level_id,maximum,people,numberOfPeople,reward,type;
    public String title,body,dateTime,dateTime_2,location,image,deadline,created_at,updated_at,slug,name,cat,district,imgUrl;


    public static Event_class getInstance(JSONObject jsonObject){
        Event_class event = new Event_class();

        event.name = jsonObject.optString("name");
        event.slug = jsonObject.optString("slug");
        event.type = jsonObject.optInt("type");
        event.reward = jsonObject.optInt("reward");
        event.cat = jsonObject.optString("cat");
        event.district = jsonObject.optString("district");
        event.location = jsonObject.optString("location");
        event.maximum = jsonObject.optInt("maximum");
        event.people = jsonObject.optInt("people");
        event.dateTime = jsonObject.optString("dateTime");
        event.imgUrl = jsonObject.optString("imgUrl");

        return event;
    }

}


