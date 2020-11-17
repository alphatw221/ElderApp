package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elderApp.ElderApp.Model_Class.Transaction_class;
import com.elderApp.ElderApp.R;

import java.util.List;

public class my_transaction_listview_adapter extends BaseAdapter {


    private LayoutInflater myInflater;
    private List<Transaction_class> list;
    Context context ;

    public my_transaction_listview_adapter(Context c, List<Transaction_class> list){
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
        convertView=myInflater.inflate(R.layout.my_transaction_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        TextView transaction_message=(TextView) convertView.findViewById(R.id._transaction_message);
        TextView transaction_object=(TextView)convertView.findViewById(R.id._transaction_object);
        TextView transaction_amount=(TextView)convertView.findViewById(R.id._transaction_amount);
        TextView transaction_date=(TextView)convertView.findViewById(R.id._transaction_date);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------

        transaction_date.setText("("+list.get(position).created_at+")");
        if(!list.get(position).event.equals("null")){
            transaction_message.setText(list.get(position).event);
        }else{
            transaction_message.setText("無訊息");
        }
        transaction_object.setText((list.get(position).target_name));


        if(list.get(position).give_take==1){
            transaction_amount.setText("+"+Integer.toString(list.get(position).amount));
            transaction_amount.setTextColor(Color.rgb(74,181,102));
        }else{
            transaction_amount.setText("-"+Integer.toString(list.get(position).amount));
            transaction_amount.setTextColor(Color.rgb(223,75,90));
        }

        return convertView;
    }




}
