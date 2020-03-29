package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.List;

public class event_listview_adapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Event_class> list;
    Context context ;

    public event_listview_adapter(Context c, JSONArray jsonArray){
        myInflater=LayoutInflater.from(c);
        list= jasonList_2_objList.convert_2_Event_list(c,jsonArray);
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
        convertView=myInflater.inflate(R.layout.event_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        ImageView event_image=(ImageView)convertView.findViewById(R.id._event_image);
        TextView event_title=(TextView)convertView.findViewById(R.id._event_title);
        TextView event_category=(TextView)convertView.findViewById(R.id._event_category);
        TextView event_location=(TextView)convertView.findViewById(R.id._event_location);
        TextView event_people=(TextView)convertView.findViewById(R.id._event_people);
        TextView event_dateTime=(TextView)convertView.findViewById(R.id._event_dateTime);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        String url="https://www.happybi.com.tw/images/events/"+list.get(position).slug+"/"+list.get(position).image;
        Picasso.get().load(url).into(event_image);

        event_title.setText(list.get(position).title);
        event_category.setText(Integer.toString(list.get(position).category_id));
        event_location.setText(list.get(position).location);
        event_people.setText(Integer.toString(list.get(position).people));
        event_dateTime.setText(list.get(position).dateTime);
        return convertView;
    }
}
