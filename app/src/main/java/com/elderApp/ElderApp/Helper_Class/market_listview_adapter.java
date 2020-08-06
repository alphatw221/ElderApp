package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elderApp.ElderApp.Model_Class.Product_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class market_listview_adapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Product_class> list;
    Context context ;

    public market_listview_adapter(Context c, List<Product_class> list)
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
        convertView=myInflater.inflate(R.layout.market_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        ImageView product_image=(ImageView)convertView.findViewById(R.id._product_image);
        TextView market_listview_name=(TextView)convertView.findViewById(R.id._market_listview_name);
        TextView market_listview_price=(TextView)convertView.findViewById(R.id._market_listview_price);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        String url=list.get(position).imgUrl;
        Picasso.get().load(url).into(product_image);

        market_listview_name.setText("商品："+list.get(position).name);
        market_listview_price.setText("樂幣："+Integer.toString(list.get(position).price));
        return convertView;
    }
}
