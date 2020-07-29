package com.example.myapplication.Helper_Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Location_class;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class location_listview_adapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private JSONArray locationList;
    Context context ;

    public location_listview_adapter(Context c, JSONArray jsonArray )
    {
        myInflater=LayoutInflater.from(c);
        this.locationList=jsonArray;
    }

    @Override
    public int getCount() {
        return locationList.length();
    }

    @Override
    public Object getItem(int position) {
        Location_class location_class=new Location_class();
        try{
            location_class.name=locationList.getJSONObject(position).getString("name");
            location_class.address=locationList.getJSONObject(position).getString("address");
            location_class.slug=locationList.getJSONObject(position).getString("slug");
            location_class.link=locationList.getJSONObject(position).getString("link");
        }catch (JSONException e){
            new AlertDialog.Builder(context)
                    .setTitle("錯誤")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(e.toString())
                    .show();
        }
        return location_class;
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
//        Location_class lc=list.get(position);
        try {
            String location=locationList.getJSONObject(position).getString("name");
            Integer amount=locationList.getJSONObject(position).getInt("quantity");
            product_detail_listview_location.setText(location+"(數量："+amount.toString()+")");
        }catch (JSONException c){

        }
//        product_detail_listview_location.setText(locationList.getJSONObject(position).getString("name")+"(數量："+Integer.toString(location_dic.get(lc.id))+")");
////        product_detail_listview_location.setText(list.get(position).name);

        return convertView;
    }
}
