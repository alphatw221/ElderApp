package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;


public class apiService {

    private static String host = "https://www.happybi.com.tw";

    public static void getAssociationRequest(Context context, Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener){
        System.out.println("getAssociationRequest");
        String requestUrl = host.concat("/api/getAllAssociation");
        System.out.println(requestUrl);
        //new JsonArrayRequest(Re)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,requestUrl,null,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(jsonArrayRequest);
    }

}
