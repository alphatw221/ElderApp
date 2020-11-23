package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ErrorHandler {

    public static void alert(Context context){
        ErrorHandler.alert(context,"訊息","系統錯誤");
    }
    public static void alert(Context context,String title,String message){
        new ios_style_alert_dialog_1.
                Builder(context)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    public static Response.ErrorListener defaultListener(final Context context){
        Response.ErrorListener listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.alert(context);
            }
        };

        return listener;
    }

    public static String errorMessage(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            return responseBody;
        }catch (UnsupportedEncodingException errorr) { }
        return null;
    }
}
