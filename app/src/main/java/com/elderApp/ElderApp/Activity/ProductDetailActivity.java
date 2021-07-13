package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.CustomComponents.LocationCell;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.ProductDetailDelegate;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Model_Class.LocationQuantity_class;
import com.elderApp.ElderApp.Model_Class.Location_class;
import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailDelegate {

    private Context context;
    private String slug;
    private String type = "free";

    private Product_class product_class;

    private ImageButton product_detail_back;
    private Button product_detail_exchange,product_detail_purchase;
    private TextView product_detail_name,product_detail_price;
    private ImageView product_detail_image;
    private View pay_cash_view;
    private TextView pay_cash_price;
    private TextView pay_cash_point;
    private View cash_view;
    private TextView cash;
    private TextView original_cash;
    private Button share_button;

    private RadioGroup prodcut_detail_radio_group;
    private LinearLayout location_outter;

    private List<Location_class> location_list = new ArrayList<>();
    private List<LocationQuantity_class> locationquantity_list;
    private Map<Integer,Integer> location_Dic = new HashMap<Integer, Integer>();
    private List<String> location_string_list = new ArrayList<String>();

    List<String> location_name_list = new ArrayList<>();
    List<String> location_slug_list = new ArrayList<>();
    List<Integer> location_id_list = new ArrayList<>();
    private String buynowUrl;
    private WebView product_detail_webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Intent intent = getIntent();

        if(intent.getAction() != null){
            // ATTENTION: This was auto-generated to handle app links.
            String appLinkAction = intent.getAction();
            Uri appLinkData = intent.getData();
            String path = appLinkData.getPath();
            System.out.println("App link test");
            System.out.println(path);

            String[] str = path.split("/");
            if(str.length >= 4){
                slug = str[3];
            }
        }else{
            slug = intent.getStringExtra("slug");
            type = intent.getStringExtra("type");
        }


        setContentView(R.layout.fragment_product_detail);
        product_detail_name=(TextView)findViewById(R.id._product_detail_name);
        product_detail_price=(TextView)findViewById(R.id._product_detail_price);

        product_detail_image=(ImageView)findViewById(R.id._product_detail_image);
        product_detail_back=(ImageButton)findViewById(R.id._product_detail_back);


        product_detail_exchange=(Button)findViewById(R.id._product_detail_exchange);
        product_detail_purchase=(Button)findViewById(R.id._product_detail_purchase);
        product_detail_webview=findViewById(R.id._product_detail_webview);
        prodcut_detail_radio_group=findViewById(R.id._product_detail_radio_group);
        location_outter = findViewById(R.id.location_outter);
        share_button = findViewById(R.id.share_button);

        pay_cash_view = (View)findViewById(R.id.pay_cash_view);
        pay_cash_price = (TextView)findViewById(R.id.pay_cash_price);
        pay_cash_point = (TextView)findViewById(R.id.pay_cash_point);
        cash_view = (View)findViewById(R.id.cash_view);
        cash = (TextView)findViewById(R.id.cash);
        original_cash = (TextView)findViewById(R.id.original_cash);
        original_cash.setPaintFlags(original_cash.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


        product_detail_price.setVisibility(View.GONE);
        cash_view.setVisibility(View.GONE);
        pay_cash_view.setVisibility(View.GONE);

        switch(type){
            case "free":
                product_detail_exchange.setOnClickListener(exchangeListener);
                break;
            case "cash":
                product_detail_exchange.setOnClickListener(exchangeByCashListener);
                break;
            default:break;
        }

        product_detail_purchase.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",buynowUrl + "?token=" + TabActivity.user.access_token);
                startActivity(intent);
            }
        });
        product_detail_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share_button.setOnClickListener(shareListener);

        getProduct();

    }

    private void getProduct(){
        apiService.getProductRequest(context, slug, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject product = new JSONObject();
                JSONArray locationList = new JSONArray();
                try {
                    product=response.getJSONObject("product");
                    locationList=response.getJSONArray("locationList");
                }catch (JSONException e){
                    ErrorHandler.alert(context);
                    return;
                }

                buynowUrl = product.optString("buynowUrl");
                Picasso.get().load(product.optString("imgUrl")).into(product_detail_image);
                product_detail_name.setText(product.optString("name"));
                product_detail_price.setText("樂幣："+product.optString("price"));
                pay_cash_price.setText(Integer.toString(product.optInt("pay_cash_price")));
                pay_cash_point.setText(Integer.toString(product.optInt("pay_cash_point")));
                cash.setText(Integer.toString(product.optInt("cash")));
                original_cash.setText("原價："+product.optInt("original_cash"));
                product_detail_webview.loadData(product.optString("info"),"text/html","UTF-8");

                switch(type){
                    case "free":
                        product_detail_price.setVisibility(View.VISIBLE);
                        break;
                    case "cash":
                        cash_view.setVisibility(View.VISIBLE);
                        pay_cash_view.setVisibility(View.VISIBLE);
                        break;
                    default:break;
                }

                for(int i=0; i < locationList.length(); i++){
                    try {
                        JSONObject location = locationList.getJSONObject(i);
                        String address = location.getString("address");
                        String name = location.getString("name");
                        String link = location.getString("link");
                        int quantity = location.getInt("quantity");
                        if(type.equals("cash")){
                            quantity = location.getInt("quantity_cash");
                        }
                        int location_id = location.getInt("location_id");
                        LocationCell cell = new LocationCell(context,location_id,name,address,link,quantity);
                        cell.isClickable();
                        cell.delegate = (ProductDetailDelegate)context;
                        location_outter.addView(cell);
                    }catch (JSONException e){
                        continue;
                    }
                }

            }
        }, ErrorHandler.defaultListener(context));
    }

    /**確認兌換（樂幣） */
    private Button.OnClickListener exchangeListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            int selected_location_id = getSelected_location_id();
            if(selected_location_id == 0){
                ErrorHandler.alert(context,"訊息","請選擇兌換據點");
                return;
            }

            apiService.purchaseProductRequest(context, slug, selected_location_id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ErrorHandler.alert(context,"訊息","兌換成功");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = ErrorHandler.errorMessage(error);
                    ErrorHandler.alert(context,"訊息",message);
                }
            });

        }
    };

    /**確認兌換(現金) */
    private Button.OnClickListener exchangeByCashListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

    private int getSelected_location_id(){
        for(int i = 0; i < location_outter.getChildCount(); i++){
            LocationCell cell = (LocationCell) location_outter.getChildAt(i);
            if(cell.isChecked){  return cell.location_id; }
        }
        return 0;
    }

    @Override
    public void selectLocation(int location_id) {
        for(int i = 0; i < location_outter.getChildCount(); i++){
            LocationCell cell = (LocationCell) location_outter.getChildAt(i);
            if(cell.location_id != location_id){
                cell.setChecked(false);
            }
        }
    }

    @Override
    public void showLocationDetail(String name, String address, String link) {
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("address",address);
        intent.putExtra("link",link);
        intent.setClass(ProductDetailActivity.this, LocationDetail.class);
        startActivity(intent);
    }

    private Button.OnClickListener shareListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String urlString = apiService.host + "/app/product/" + slug;
            sendIntent.putExtra(Intent.EXTRA_TEXT, urlString);
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    };


}