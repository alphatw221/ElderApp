package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Helper_Class.market_listview_adapter;
import com.elderApp.ElderApp.Helper_Class.market_myOrder_listview_adapter;
import com.elderApp.ElderApp.Model_Class.Order_class;
import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarketActivity extends AppCompatActivity {



    private Context context;


    private ListView market_listView,order_listView;
    private Button market_product,market_myProduct;
    private ImageButton market_back;

    private int product_page = 1;
    private boolean product_hasNextPage = true;

    private int order_page = 1;
    private boolean order_hasNextPage = true;

    private List<Product_class> productList = new ArrayList<Product_class>();
    private List<Order_class> orderList = new ArrayList<Order_class>();

    private market_listview_adapter productListAdapter;
    private market_myOrder_listview_adapter orderListAdapter;

    private ProgressBar market_progressbar;
    /**列表type (free=>兌換;cash=>商城) */
    private String listType = "free";
    /**上方tab列表*/
    private androidx.constraintlayout.widget.ConstraintLayout tabBar;
    /**訂單記錄按鈕*/
    private Button myOrderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_market_);
        context = this;

        if(getIntent().hasExtra("listType")){
            listType = (String)getIntent().getExtras().get("listType");
        }

        myOrderButton = (Button)findViewById(R.id.myOrderButton);
        market_listView = (ListView)findViewById(R.id._market_listView);
        order_listView = (ListView)findViewById(R.id._order_listView);
        market_product = (Button)findViewById(R.id._market_product);
        market_myProduct = (Button)findViewById(R.id._market_myProduct);
        market_back = (ImageButton)findViewById(R.id._market_back);
        market_progressbar = findViewById(R.id._market_progressbar);

        productListAdapter = new market_listview_adapter(context,productList,listType);
        market_listView.setAdapter(productListAdapter);
        market_listView.setOnScrollListener(onScrollProductListener);
        market_listView.setOnItemClickListener(onItemClickListener);

        orderListAdapter = new market_myOrder_listview_adapter(context,orderList);
        order_listView.setAdapter(orderListAdapter);
        order_listView.setOnScrollListener(onScrollOrderListener);
        tabBar = findViewById(R.id.tabBar);
        switch (listType){
            case "free":
                tabBar.setVisibility(View.VISIBLE);
                myOrderButton.setVisibility(View.GONE);
                break;
            case "cash":
                tabBar.setVisibility(View.GONE);
                myOrderButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        myOrderButton.setOnClickListener(myOrderListener);
        market_product.setOnClickListener(switcherListener);
        market_myProduct.setOnClickListener(switcherListener);
        market_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        getProductList();
    }

    private void getProductList(){
        product_hasNextPage = false;
        switch (listType){
            case "free":
                apiService.getProductListRequest(context, product_page, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response);
                    }
                }, ErrorHandler.defaultListener(context));
                break;
            case "cash":
                apiService.getMarketProductListRequest(context, product_page, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response);
                    }
                }, ErrorHandler.defaultListener(context));
                break;
            default:
                break;
        }
    }

    private void handleResponse(JSONObject response){
        JSONArray jsonArray=new JSONArray();
        try {
            jsonArray=response.getJSONArray("productList");
            product_hasNextPage=response.getBoolean("hasNextPage");
        }catch (JSONException e){
            ErrorHandler.alert(context);
            return;
        }

        productList.addAll(jasonList_2_objList.productList(jsonArray));
        product_page++;
        productListAdapter.notifyDataSetChanged();
        market_progressbar.setVisibility(View.GONE);
    }


    private void getOrderList(){
        order_hasNextPage = false;
        apiService.getMyOrderRequest(context, order_page, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = response.getJSONArray("orderList");
                    order_hasNextPage = response.getBoolean("hasNextPage");
                }catch (JSONException e){
                    ErrorHandler.alert(context);
                    return;
                }

                orderList.addAll(jasonList_2_objList.orderList(jsonArray));
                order_page++;
                orderListAdapter.notifyDataSetChanged();
            }
        },ErrorHandler.defaultListener(context));
    }


    private Button.OnClickListener switcherListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._market_product:
                    market_product.setOnClickListener(null);
                    market_product.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_myProduct.setOnClickListener(switcherListener);
                    market_myProduct.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_listView.setVisibility(View.VISIBLE);
                    order_listView.setVisibility(View.GONE);
                    break;
                case R.id._market_myProduct:
                    market_myProduct.setOnClickListener(null);
                    market_myProduct.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    market_product.setOnClickListener(switcherListener);
                    market_product.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    market_listView.setVisibility(View.GONE);
                    order_listView.setVisibility(View.VISIBLE);
                    orderList.clear();
                    order_page=1;
                    getOrderList();
                    break;
            }

        }
    };

    private ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Product_class product = (Product_class)parent.getItemAtPosition(position);

            Intent intent = new Intent(context,ProductDetailActivity.class);
            intent.putExtra("slug",product.slug);
            intent.putExtra("type",listType);
            startActivity(intent);
        }
    };




    private ListView.OnScrollListener onScrollProductListener = new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) { }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(totalItemCount == 0){return;}
            if(!product_hasNextPage){ return; }
            if(totalItemCount - visibleItemCount == firstVisibleItem){
                getProductList();
            }

        };
    };

    private ListView.OnScrollListener onScrollOrderListener = new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) { }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(totalItemCount == 0){return;}
            if(!order_hasNextPage){ return; }
            if(totalItemCount - visibleItemCount == firstVisibleItem){
                getOrderList();
            }

        };
    };

    /**
     * 訂單記錄事件
     */
    private View.OnClickListener myOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url",apiService.host +"/order/list" + "?token=" + TabActivity.user.access_token + "&noFooter=1");
            startActivity(intent);
        }
    };


}