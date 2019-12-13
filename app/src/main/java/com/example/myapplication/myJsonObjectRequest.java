package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class myJsonObjectRequest {
    private String _url;
    private String _mode; // get , post , put , delete
    private int _mode_int;
    private Object[] _key,_value;
    private JSONObject _jsonObject;
    private Context _context;
    private Response.Listener<JSONObject> _RL;
    private Response.ErrorListener _REL;
    private RequestQueue _queue;

    public myJsonObjectRequest(){

    }
    public myJsonObjectRequest(String url,String mode,Context context,Response.Listener<JSONObject> RL,Response.ErrorListener REL){
        _url=url;
        _mode=mode;
        _RL=RL;
        _REL=REL;
        _context=context;
        _queue = MySingleton.getInstance(_context).getRequestQueue();
        get_mode();
    }
    public myJsonObjectRequest(String url,String mode,Object[] key,Object[] value,Context context,Response.Listener<JSONObject> RL,Response.ErrorListener REL){
        _url=url;
        _mode=mode;
        _key=key;
        _value=value;
        _RL=RL;
        _REL=REL;
        _context=context;
        _queue = MySingleton.getInstance(_context).getRequestQueue();
        get_JSONObject();
        get_mode();
    }


    private void get_JSONObject(){
        try{
            _jsonObject=new JSONObject();
            for(int i=0;i<_key.length;i++){
                _jsonObject.put(_key[i].toString(),_value[i].toString());
            }
        }catch(JSONException e){

        }
    }
    private void get_mode(){
        if(_mode.equals("post")){
            _mode_int=1;
        }else if (_mode.equals("put")){
            _mode_int=2;
        }else if(_mode.equals("delete")){
            _mode_int=3;
        }else{
            _mode_int=0;
        }
    }
    public void Fire(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(_mode_int,_url,_jsonObject,_RL,_REL){
            @Override
            public Map<String, String> getHeaders() {
               Map<String, String> headers = new HashMap<String, String>();
               headers.put("Content-Type", "application/x-www-form-urlencoded");
               headers.put("Content-Type", "application/json");
               return headers;
            }
        };
        _queue.add(jsonObjectRequest);
    }

}
