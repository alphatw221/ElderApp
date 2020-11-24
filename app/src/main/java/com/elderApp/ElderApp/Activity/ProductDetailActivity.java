package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
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

public class ProductDetailActivity extends AppCompatActivity {

    private Context context;
    private String slug;

    private Product_class product_class;

    private ImageButton product_detail_back;
    private Button product_detail_exchange,product_detail_purchase;
    private TextView product_detail_name,product_detail_price;
    private ImageView product_detail_image;


    private RadioGroup prodcut_detail_radio_group;

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
        slug = getIntent().getStringExtra("slug");

        setContentView(R.layout.fragment_product_detail);
        product_detail_name=(TextView)findViewById(R.id._product_detail_name);
        product_detail_price=(TextView)findViewById(R.id._product_detail_price);

        product_detail_image=(ImageView)findViewById(R.id._product_detail_image);
        product_detail_back=(ImageButton)findViewById(R.id._product_detail_back);


        product_detail_exchange=(Button)findViewById(R.id._product_detail_exchange);
        product_detail_purchase=(Button)findViewById(R.id._product_detail_purchase);
        product_detail_webview=findViewById(R.id._product_detail_webview);
        prodcut_detail_radio_group=findViewById(R.id._product_detail_radio_group);

        product_detail_exchange.setOnClickListener(exchangeListener);
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
                product_detail_name.setText("商品："+product.optString("name"));
                product_detail_price.setText("樂幣："+product.optString("price"));
                product_detail_webview.loadData(product.optString("info"),"text/html","UTF-8");


                for(int i=0; i < locationList.length(); i++){
                    View radioOptionView = LayoutInflater.from(context).inflate(R.layout.radio_btn_location_layout,null);
                    try {
                        JSONObject location = locationList.getJSONObject(i);
                        RadioButton radioButton = (RadioButton)radioOptionView.findViewById(R.id._radio_btn_location);
                        radioButton.setText(location.getString("name") + "(數量:" + location.getInt("quantity") + ")");
                        radioButton.setId(location.getInt("location_id"));
                    }catch (JSONException e){
                        continue;
                    }
                    prodcut_detail_radio_group.addView(radioOptionView);
                }

            }
        }, ErrorHandler.defaultListener(context));
    }


    private Button.OnClickListener exchangeListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(prodcut_detail_radio_group.getCheckedRadioButtonId() == -1) {
                ErrorHandler.alert(context,"訊息","請選擇兌換據點");
                return;
            }

            int location_id = prodcut_detail_radio_group.getCheckedRadioButtonId();
            apiService.purchaseProductRequest(context, slug, location_id, new Response.Listener<String>() {
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

}