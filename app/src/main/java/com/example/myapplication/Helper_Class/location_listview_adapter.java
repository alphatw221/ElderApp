package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Location_class;
import com.example.myapplication.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class location_listview_adapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Location_class> list;
    private Map<Integer,Integer> location_dic;
    Context context ;

    public location_listview_adapter(Context c, List<Location_class> lc_list, Map<Integer,Integer> ld )
    {
        myInflater=LayoutInflater.from(c);
        list=lc_list;
        location_dic=ld;
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
        convertView=myInflater.inflate(R.layout.product_detail_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        TextView product_detail_listview_location=(TextView)convertView.findViewById(R.id._product_detail_listview_location);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        Location_class lc=list.get(position);
        product_detail_listview_location.setText(lc.name+"(數量："+Integer.toString(location_dic.get(lc.id))+")");
//        product_detail_listview_location.setText(list.get(position).name);

        return convertView;
    }
}
