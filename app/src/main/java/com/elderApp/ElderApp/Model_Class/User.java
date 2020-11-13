package com.elderApp.ElderApp.Model_Class;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public int user_id;
    public String id_code;
    public String email;
    public String password;
    public String name;
    public int wallet;
    public int rank;
    public String img;
    public String access_token;
    public int org_rank;

    public User(){

    }

    public static User getInstance(JSONObject response){
        User user = new User();

        user.access_token = response.optString("access_token");
        user.user_id = response.optInt("user_id");
        user.id_code = response.optString("id_code");
        user.email = response.optString("email");
        user.name = response.optString("name");
        user.wallet = response.optInt("wallet");
        user.rank = response.optInt("rank");
        user.org_rank=response.optInt("org_rank");
        
        return user;
    }
}
