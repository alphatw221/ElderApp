package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.Activity.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class apiService {

    public static String host = "https://app.happybi.com.tw";

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

    /**
     * 帶有使用者jwt的請求(回傳 String)
     * @param context
     * @param requestUrl
     * @param method
     * @param postData
     * @param responseListener
     * @param errorListener
     * @return
     */
    private static StringRequest AuthorizationStringRequest(final Context context, String requestUrl,int method, JSONObject postData, Response.Listener<String> responseListener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(method,requestUrl,responseListener,errorListener){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return postData.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                String token = TabActivity.user.access_token;
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
     * 取得所有的活動fra
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

    /**
     * 取得可兌換商品列表
     * @param context
     * @param page
     * @param responseListener
     * @param errorListener
     */
    public static void getProductListRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getProductListRequest");
        String requestUrl = host + "/api/productList" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得可購買商品列表
     * @param context
     * @param page
     * @param responseListener
     * @param errorListener
     */
    public static void getMarketProductListRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMarketProductListRequest");
        String requestUrl = host + "/api/marketProductList" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得我的兌換紀錄
     * @param context
     * @param page
     * @param responseListener
     * @param errorListener
     */
    public static void getMyOrderRequest(Context context,int page,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getMyOrderRequest");
        String requestUrl = host + "/api/order/myOrderList" + "?page=" + page;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 取得產品詳細資料
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void getProductRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("getProductRequest");
        String requestUrl = host + "/api/product/productDetail/" + slug;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 兌換產品
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void purchaseProductRequest(final Context context, String slug, final int location_id, Response.Listener<String> responseListener, Response.ErrorListener errorListener){
        System.out.println("purchaseProductRequest");
        String requestUrl = host + "/api/purchase/" + slug;
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String token = context.getSharedPreferences("preFile", context.MODE_PRIVATE).getString("access_token", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",token);
                params.put("location_id",String.valueOf(location_id));
                return params;
            }
        };
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 購買產品
     * @param context
     * @param slug
     * @param location_id
     * @param quantity
     * @param responseListener
     * @param errorListener
     */
    public static void purchaseProductByCashRequest(final Context context, String slug, final int location_id,final int quantity, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        System.out.println("purchaseProductByCashRequest");
        String requestUrl = host + "/api/purchaseByCash/" + slug;
        JSONObject postData = new JSONObject();
        try{
            postData.put("location_id", location_id);
            postData.put("quantity", quantity);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 交易
     * @param context
     * @param take_id
     * @param take_email
     * @param amount
     * @param message
     * @param responseListener
     * @param errorListener
     */
    public static void transactionRequest(final Context context, final String take_id, final String take_email, final String amount,final String message, Response.Listener<String> responseListener, Response.ErrorListener errorListener){
        System.out.println("transactionRequest");
        String requestUrl = host + "/api/transaction/";
        JSONObject postData = new JSONObject();
        try{
            postData.put("give_id", TabActivity.user.user_id);
            postData.put("give_email", TabActivity.user.email);
            postData.put("take_id", Integer.parseInt(take_id));
            postData.put("take_email", take_email);
            postData.put("amount", Integer.parseInt(amount));
            postData.put("event",message);
        }catch (JSONException e){
            return;
        }
        StringRequest request = AuthorizationStringRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }


    /**
     * 領取獎勵
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void takeRewardRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("takeRewardRequest");
        String requestUrl = host + "/api/drawEventRewardV2/" + slug;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 查詢是否簽到
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void isUserArriveRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("isUserArriveRequest");
        String requestUrl = host + "/api/isUserArrive/" + slug;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.GET,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 進行簽到
     * @param context
     * @param slug
     * @param responseListener
     * @param errorListener
     */
    public static void arriveEventRequest(Context context,String slug,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("arriveEventRequest");
        String requestUrl = host + "/api/arriveEvent/" + slug;
        JSONObject postData = new JSONObject();
        JsonObjectRequest request = AuthorizationPostRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * 綁定Line 帳號請求
     * @param context
     * @param userId
     * @param responseListener
     * @param errorListener
     */
    public static void bindLineAccountRequest(Context context,String userId,Response.Listener<String> responseListener,Response.ErrorListener errorListener){
        System.out.println("bindLineAccountRequest");
        String requestUrl = host + "/api/auth/bind_lineAccount";
        JSONObject postData = new JSONObject();
        try{
            postData.putOpt("userID",userId);
        }catch (JSONException e){
            return;
        }
        StringRequest request = AuthorizationStringRequest(context,requestUrl,Request.Method.POST,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Line 登入
     * @param context
     * @param userId
     * @param responseListener
     * @param errorListener
     */
    public static void lineLoginRequest(Context context,String userId,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
        System.out.println("lineLoginRequest");
        String requestUrl = host + "/api/auth/line_login";
        JSONObject postData = new JSONObject();
        try{
            postData.putOpt("userID",userId);
        }catch (JSONException e){
            return;
        }
        JsonObjectRequest request = DefaultPostRequest(requestUrl,postData,responseListener,errorListener);
        MySingleton.getInstance(context).getRequestQueue().add(request);
    }


}
