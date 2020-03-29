package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class market_listview_adapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Product_class> list;
    Context context ;

    public market_listview_adapter(Context c, JSONObject jsonObject)
    {
        myInflater=LayoutInflater.from(c);
        list= jasonList_2_objList.convert_2_Product_list(c,jsonObject);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=myInflater.inflate(R.layout.market_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        ImageView product_image=(ImageView)convertView.findViewById(R.id._product_image);
        TextView market_listview_name=(TextView)convertView.findViewById(R.id._market_listview_name);
        TextView market_listview_price=(TextView)convertView.findViewById(R.id._market_listview_price);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        String url="https://www.happybi.com.tw/images/products/"+list.get(position).slug+"/"+list.get(position).img;
        Picasso.get().load(url).into(product_image);

        market_listview_name.setText(list.get(position).name);
        market_listview_price.setText(Integer.toString(list.get(position).price));
        return convertView;
    }
}
