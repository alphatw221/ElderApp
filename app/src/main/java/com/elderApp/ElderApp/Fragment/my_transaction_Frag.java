package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Model_Class.Transaction_class;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.my_transaction_listview_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class my_transaction_Frag extends Fragment {

    private ListView my_transaction_listview;
    private String url="https://www.happybi.com.tw/api/transaction/myTransactionHistory/";
    private int page=1;
    private boolean hasNextPage;

    private Context context;
    private User user;
    private ImageButton backButton;
    private my_transaction_listview_adapter adapter;
    private ProgressBar my_transaction_progressbar;
    public List<Transaction_class> tran_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_my_transaction,container,false);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        my_transaction_listview=(ListView)view.findViewById(R.id._my_transaction_listview);
        backButton = view.findViewById(R.id._my_transaction_back);
        my_transaction_progressbar=view.findViewById(R.id._my_transaction_progressbar);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
//        tran_list=new ArrayList<Transaction_class>();
        context=this.getContext();
        user=(User) getActivity().getIntent().getSerializableExtra("User");
        backButton.setOnClickListener(backListener);
        my_transaction_listview.setOnItemClickListener(null);
        my_transaction_listview.setOnScrollListener(onScrollListener);
        //---------------------發出請求------------------------------------------------------------

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url+"?page="+page,null,RL_JA,REL){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };
        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
        //----------------------------------------------------------------------------------------------------------------------------------------------------

        return view;
    }
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL_JA=new Response.Listener<JSONObject>(){

        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonArray=new JSONArray();
            try {
                jsonArray=response.getJSONArray("transList");
                hasNextPage=response.getBoolean("hasNextPage");
            }catch (JSONException e){
                Log.d("json error",e.toString());
            }
            List<Transaction_class> t=new ArrayList<Transaction_class>();
            tran_list=jasonList_2_objList.convert_2_transaction_list(context,jsonArray);

            adapter=new my_transaction_listview_adapter(context,tran_list);
            my_transaction_listview.setAdapter(adapter);
            my_transaction_progressbar.setVisibility(View.GONE);


        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new ios_style_alert_dialog_1.Builder(context)
                    .setTitle("連線錯誤")
                    .setMessage("請重新登入")
                    .show();
        }
    };




    private ImageButton.OnClickListener backListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("Frag1");
            Fragment fragment2=FM.findFragmentByTag("my_transaction_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "my_transaction_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new Frag1(),"Frag1");

            }
            FT.commit();
        }
    };


    private ListView.OnScrollListener onScrollListener=new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(hasNextPage){
                if(firstVisibleItem>(totalItemCount-5)){
                    Log.d("total",Integer.toString(totalItemCount));
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url+"?page="+page,null,RL_JA2,REL2){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization","Bearer "+Token);
                            return headers;
                        }
                    };
                    MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
                    my_transaction_listview.setOnScrollListener(null);
                }
            }

        };
    };




    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL_JA2=new Response.Listener<JSONObject>(){

        @Override
        public void onResponse(JSONObject response) {
            JSONArray jsonArray=new JSONArray();
            try {
                jsonArray=response.getJSONArray("transList");
                hasNextPage=response.getBoolean("hasNextPage");
            }catch (JSONException e){
                Log.d("json error",e.toString());
            }
            List<Transaction_class>temp;
            temp=jasonList_2_objList.convert_2_transaction_list(context,jsonArray);
            tran_list.addAll(temp);
            adapter.notifyDataSetChanged();
            page++;
            my_transaction_listview.setOnScrollListener(onScrollListener);

        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL2=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new ios_style_alert_dialog_1.Builder(context)
                    .setTitle("連線錯誤")
                    .setMessage("請重新登入")
                    .show();
        }
    };
}
