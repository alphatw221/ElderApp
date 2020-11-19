package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Helper_Class.my_transaction_listview_adapter;
import com.elderApp.ElderApp.Model_Class.Transaction_class;
import com.elderApp.ElderApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTransactionActivity extends AppCompatActivity {

    private Context context;
    private ListView my_transaction_listview;
    private int page=1;
    private boolean hasNextPage = true;

    private ImageButton backButton;
    private my_transaction_listview_adapter adapter;
    private ProgressBar my_transaction_progressbar;
    public List<Transaction_class> tran_list = new ArrayList<Transaction_class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frag_my_transaction);

        context = this;
        backButton = (ImageButton)findViewById(R.id._my_transaction_back);
        my_transaction_progressbar = findViewById(R.id._my_transaction_progressbar);
        my_transaction_listview = (ListView)findViewById(R.id._my_transaction_listview);


        backButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new my_transaction_listview_adapter(context,tran_list);
        my_transaction_listview.setAdapter(adapter);
        my_transaction_listview.setOnItemClickListener(null);
        my_transaction_listview.setOnScrollListener(onScrollListener);

        getMyTransaction();
    }



    private void getMyTransaction(){

        hasNextPage = false;
        apiService.getMyTransactionRequest(this, page, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray=new JSONArray();
                try {
                    jsonArray=response.getJSONArray("transList");
                    hasNextPage=response.getBoolean("hasNextPage");
                }catch (JSONException e){
                    return;
                }

                tran_list.addAll(jasonList_2_objList.transactionList(jsonArray));
                page++;
                adapter.notifyDataSetChanged();
                my_transaction_progressbar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1
                        .Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        });

    }


    private ListView.OnScrollListener onScrollListener=new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) { }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(totalItemCount == 0){return;}
            if(!hasNextPage){ return; }
            if(totalItemCount - visibleItemCount == firstVisibleItem){
                getMyTransaction();
            }

        };
    };

}