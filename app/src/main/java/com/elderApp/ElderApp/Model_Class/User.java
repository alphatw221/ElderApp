package com.elderApp.ElderApp.Model_Class;

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
    public Integer org_rank;
    public User(){

    }
}