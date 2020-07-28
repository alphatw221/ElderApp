package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class myJsonRequest {
    public static class GET_Request{

        private static Object[] _key,_value;
        private static JSONObject _jsonObject;

        public static void getJSON_array(String url,Object[] key, Object[] value, Context context, Response.Listener<JSONArray> RL, Response.ErrorListener REL){
            _key=key;
            _value=value;
            if(key!=null){
                get_JSONObject();
            }

            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(0,url,null,RL,REL){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(jsonArrayRequest);

        }

        public static void getJSON_object(String url, Object[] key, Object[] value,Context context, Response.Listener<JSONObject> RL, Response.ErrorListener REL){
            _key=key;
            _value=value;
            if(key!=null){
                get_JSONObject();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0,url,_jsonObject,RL,REL){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    return headers;

                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
        }

        private static void get_JSONObject(){
            try{
                _jsonObject=new JSONObject();
                for(int i=0;i<_key.length;i++){
                    _jsonObject.put(_key[i].toString(),_value[i].toString());
                }
            }catch(JSONException e){

            }
        }

    }



    public static class POST_Request{

        private static Object[] _key,_value;
        private static JSONObject _jsonObject;
        private static JSONArray _jsonArray;

        public static void get_string(String url,Object[] key, Object[] value, Context context, Response.Listener<String> RL, Response.ErrorListener REL){
            _key=key;
            _value=value;
            get_JSONObject();
            StringRequest stringRequest=new StringRequest(1,url,RL,REL){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(stringRequest);
        }



        public static void getJSON_array(String url, Object[] key, Object[] value, Context context, Response.Listener<JSONArray> RL, Response.ErrorListener REL){

            _key=key;
            _value=value;
            get_JSONObject();

            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(1,url,null,RL,REL){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(jsonArrayRequest);
        }

        public static void getJSON_object(String url, Object[] key, Object[] value, Context context, Response.Listener<JSONObject> RL, Response.ErrorListener REL){

            _key=key;
            _value=value;
            get_JSONObject();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1,url,_jsonObject,RL,REL){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
        }

        private static void get_JSONObject(){
            try{
                _jsonObject=new JSONObject();
                for(int i=0;i<_key.length;i++){
                    _jsonObject.put(_key[i].toString(),_value[i].toString());
                }
            }catch(JSONException e){

            }
        }


    }


}
