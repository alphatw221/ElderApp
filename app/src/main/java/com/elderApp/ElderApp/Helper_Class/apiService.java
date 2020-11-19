package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class apiService {

    public static String host = "https://www.happybi.com.tw";

    /**
     * 預設的POST請求
     * @param requestUrl
     * @param postData
     * @param responseListener
     * @param errorListener
     * @return
     */
    private static JsonObjectRequest DefaultPostRequest(String requestUrl,JSONObject postData,Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,requestUrl,postData,responseListener,errorListener){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        return request;
    }

    /**
     * 帶有使用者jwt的請求
     * @param context
     * @param requestUrl
     * @param postData
     * @param responseListener
     * @param errorListener
     * @return
     */
    private static JsonObjectRequest AuthorizationPostRequest(final Context context, String requestUrl,int method, JSONObject postData, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(method,requestUrl,postData,responseListener,errorListener){
            @Override
            public Map<String, String> getHeaders() {
                String token = context.getSharedPreferences("preFile", context.MODE_PRIVATE).getString("access_token", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        return request;
    }



    //------------------- public methods below here -------------------

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

        JsonObjectRequest request = DefaultPostRequest(requestUrl,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 上傳手機的 push token
     * @param context
     * @param token
     * @param responseListener
     * @param errorListener
     */
    public static void uploadPushTokenRequest(Context context,String token, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        System.out.println("uploadPushTokenRequest");
        String requestUrl = host.concat("/api/auth/set_pushtoken");
        JSONObject postData = new JSONObject();
        try{
            postData.put("pushtoken", token);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
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

    /**
     * 取得使用者資料
     * @param context
     * @param responseListener
     * @param errorListener
     */
    public static void getMeRequest(Context context,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMeRequest");
        String requestUrl = host.concat("/api/auth/me");
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,new JSONObject(),responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得我的交易紀錄
     * @param context
     * @param page
     * @param responseListener
     * @param errorListener
     */
    public static void getMyTransactionRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMyTransactionRequest");
        String requestUrl = host + "/api/transaction/myTransactionHistory" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得我的帳戶資訊
     * @param context
     * @param responseListener
     * @param errorListener
     */
    public static void getMyAccountRequest(Context context,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMyAccountRequest");
        String requestUrl = host + "/api/auth/myAccount";
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得我參加的活動
     * @param context
     * @param responseListener
     * @param errorListener
     */
    public static void getMyEventRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMyEventRequest");
        String requestUrl = host + "/api/event/myEventList" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得所有的活動
     * @param context
     * @param responseListener
     * @param errorListener
     */
    public static void getAllEventRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getAllEventRequest");
        String requestUrl = host + "/api/event/eventList" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得活動詳細內容
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void getEventDetailRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getEventDetailRequest");
        String requestUrl = host + "/api/event/eventDetail/" + slug;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取消參加活動
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void cancelEventRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("cancelEventRequest");
        String requestUrl = host + "/api/cancelevent/" + slug;
        JSONObject postData = new JSONObject();
        try{
            postData.put("id",TabActivity.user.user_id);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 參加活動
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void joinEventRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("joinEventRequest");
        String requestUrl = host + "/api/joinevent/" + slug;
        JSONObject postData = new JSONObject();
        try{
            postData.put("id",TabActivity.user.user_id);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

}
