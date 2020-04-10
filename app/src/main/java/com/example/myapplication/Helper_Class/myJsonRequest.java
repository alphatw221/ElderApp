package com.example.myapplication.Helper_Class;

import android.content.Context;

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

public class myJsonRequest {
//    private String _url;
//    private String _mode; // get , post , put , delete
//    private int _mode_int;
//    private Object[] _key,_value;
//    private JSONObject _jsonObject;
//    private JSONArray _jsonArray;
//    private Context _context;
//    private Response.Listener<JSONObject> _RL;
//    private Response.Listener<JSONArray> _RL_JA;
//    private Response.ErrorListener _REL;
//    private RequestQueue _queue;

//    public myJsonRequest(){
//
//    }
//    public myJsonRequest(String url, String mode, Context context, Response.Listener<JSONObject> RL, Response.ErrorListener REL){
//        _url=url;
//        _mode=mode;
//        _RL=RL;
//        _REL=REL;
//        _context=context;
//        _queue = MySingleton.getInstance(_context).getRequestQueue();
//        get_mode();
//    }
//    public myJsonRequest(String url, String mode, Context context, Response.Listener<JSONArray> RL_JA, Response.ErrorListener REL,int tf){
//        _url=url;
//        _mode=mode;
//        _RL_JA=RL_JA;
//        _REL=REL;
//        _context=context;
//        _queue = MySingleton.getInstance(_context).getRequestQueue();
//        get_mode();
//    }
//    public myJsonRequest(String url, String mode, Object[] key, Object[] value, Context context, Response.Listener<JSONObject> RL, Response.ErrorListener REL){
//        _url=url;
//        _mode=mode;
//        _key=key;
//        _value=value;
//        _RL=RL;
//        _REL=REL;
//        _context=context;
//        _queue = MySingleton.getInstance(_context).getRequestQueue();
//        get_JSONObject();
//        get_mode();
//    }

//
//    private void get_JSONObject(){
//        try{
//            _jsonObject=new JSONObject();
//            for(int i=0;i<_key.length;i++){
//                _jsonObject.put(_key[i].toString(),_value[i].toString());
//            }
//        }catch(JSONException e){
//
//        }
//    }
//    private void get_JSONArray(){
//
//    }
//    private void get_mode(){
//        if(_mode.equals("post")){
//            _mode_int=1;
//        }else if (_mode.equals("put")){
//            _mode_int=2;
//        }else if(_mode.equals("delete")){
//            _mode_int=3;
//        }else{
//            _mode_int=0;
//        }
//    }
//    public void Fire(){
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(_mode_int,_url,_jsonObject,_RL,_REL){
//            @Override
//            public Map<String, String> getHeaders() {
//               Map<String, String> headers = new HashMap<String, String>();
//               headers.put("Content-Type", "application/x-www-form-urlencoded");
//               headers.put("Content-Type", "application/json");
//               return headers;
//            }
//        };
//        _queue.add(jsonObjectRequest);
//    }
//    public void Fire2(){
//        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(_mode_int,_url,null,_RL_JA,_REL){
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//        _queue.add(jsonArrayRequest);
//    }



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
