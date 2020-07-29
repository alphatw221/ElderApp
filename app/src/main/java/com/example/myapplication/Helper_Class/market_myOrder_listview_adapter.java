package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Order_class;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class market_myOrder_listview_adapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Order_class> list;
    Context context ;

    public market_myOrder_listview_adapter(Context c, List<Order_class> list)
    {
        myInflater=LayoutInflater.from(c);
        this.list=list;
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
        convertView=myInflater.inflate(R.layout.market_myorder_listview_layout,null);
        Order_class order_class=list.get(position);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        ImageView market_myorder_listview_image=(ImageView)convertView.findViewById(R.id._market_myorder_listview_image);
        ImageView market_myorder_listview_recieve=(ImageView)convertView.findViewById(R.id._market_myorder_listview_recieve);
        TextView market_myorder_listview_name=(TextView)convertView.findViewById(R.id._market_myorder_listview_name);
        TextView market_myorder_listview_location=(TextView)convertView.findViewById(R.id._market_myorder_listview_location);
        TextView market_myorder_listview_address=(TextView)convertView.findViewById(R.id._market_myorder_listview_address);
        TextView market_myorder_listview_time=(TextView)convertView.findViewById(R.id._market_myorder_listview_time);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        String url=order_class.imgUrl;
        if(order_class.receive==1){
            market_myorder_listview_recieve.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(url).into(market_myorder_listview_image);
        market_myorder_listview_name.setText("產品:"+order_class.name);
        market_myorder_listview_location.setText("據點:"+order_class.location_name);
        market_myorder_listview_address.setText("地址:"+order_class.address);
        market_myorder_listview_time.setText(("兌換時間:"+order_class.created_at));
        return convertView;
    }
}
