package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.graphics.Paint;
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
    String listType = "free";
    Context context ;

    public market_listview_adapter(Context c, List<Product_class> list,String listType)
    {
        myInflater=LayoutInflater.from(c);
        this.listType = listType;
        this.list = list;
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

        TextView pay_cash_text=(TextView)convertView.findViewById(R.id.pay_cash_text);

        View pay_cash_view = (View)convertView.findViewById(R.id.pay_cash_view);
        TextView pay_cash_price=(TextView)convertView.findViewById(R.id.pay_cash_price);
        TextView pay_cash_point=(TextView)convertView.findViewById(R.id.pay_cash_point);

        TextView cash_text=(TextView)convertView.findViewById(R.id.cash_text);

        View cash_view = (View)convertView.findViewById(R.id.cash_view);
        TextView cash=(TextView)convertView.findViewById(R.id.cash);
        TextView original_cash=(TextView)convertView.findViewById(R.id.original_cash);

        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------

        Product_class product = list.get(position);

        String url = product.imgUrl;
        Picasso.get().load(url).into(product_image);

        market_listview_name.setText(product.name);
        market_listview_price.setText("樂幣："+Integer.toString(product.price));

        pay_cash_price.setText(Integer.toString(product.pay_cash_price));
        pay_cash_point.setText(Integer.toString(product.pay_cash_point));
        cash.setText(Integer.toString(product.cash));
        original_cash.setText("原價：" + Integer.toString(product.original_cash));

        //style overline
        original_cash.setPaintFlags(original_cash.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        switch (listType){
            case "free":
                pay_cash_view.setVisibility(View.GONE);
                pay_cash_text.setVisibility(View.GONE);

                cash_view.setVisibility(View.GONE);
                cash_text.setVisibility(View.GONE);

                break;
            case "cash":
                market_listview_price.setVisibility(View.GONE);
                break;
            default:break;
        }

        return convertView;
    }
}
