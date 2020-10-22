package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class apiService {

    private static String host = "https://www.happybi.com.tw";

    /**
     * 登入
     * @param context
     * @param email
     * @param password
     * @param versionCode
     * @param responseListener
     * @param errorListener
     */
    public static void loginRequest(Context context,String email,String password,int versionCode, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        System.out.println("loginRequest");
        String requestUrl = host.concat("/api/auth/login");
        JSONObject postData = new JSONObject();
        try{
            postData.put("email", email);
            postData.put("password", password);
            postData.put("androidVer", versionCode);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,requestUrl,postData,responseListener,errorListener){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得所有可選擇的組織
     * @param context
     * @param responseListener
     * @param errorListener
     */
    public static void getAssociationRequest(Context context, Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener){
        System.out.println("getAssociationRequest");
        String requestUrl = host.concat("/api/getAllAssociation");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,requestUrl,null,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

}
