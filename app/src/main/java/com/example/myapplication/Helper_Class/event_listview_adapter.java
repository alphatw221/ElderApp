package com.example.myapplication.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

public class event_listview_adapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Event_class> list;
    private String[] districts={"0","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
    private String[] categories={"0","1","2","3","4","歡樂旅遊","樂活才藝","健康課程","8","社會服務","天使培訓","長照據點","大型活動"};
    private Context context ;


    public event_listview_adapter(Context c, List<Event_class> list){
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
        convertView=myInflater.inflate(R.layout.event_listview_layout,null);

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        ImageView event_image=(ImageView)convertView.findViewById(R.id._event_image);
        TextView event_title=(TextView)convertView.findViewById(R.id._event_title);
        TextView event_category_location=(TextView)convertView.findViewById(R.id._event_category_location);
        TextView event_happybi=(TextView)convertView.findViewById(R.id._event_happybi);
        TextView event_people=(TextView)convertView.findViewById(R.id._event_people);
        TextView event_dateTime=(TextView)convertView.findViewById(R.id._event_dateTime);
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
//        String url="https://www.happybi.com.tw/images/events/"+list.get(position).slug+"/"+list.get(position).image;
//        Picasso.get().load(url).resize(300,300).centerCrop().into(event_image);
//        event_title.setText(list.get(position).title);
//        event_happybi.setText(list.get(position).reward);
//        event_category_location.setText(categories[list.get(position).category_id]+"("+districts[list.get(position).district_id]);
//        event_people.setText(Integer.toString(list.get(position).people)+"/"+Integer.toString(list.get(position).maximum));
//        if(list.get(position).dateTime.equals("null")){
//            event_dateTime.setText("");
//        }else{
//            event_dateTime.setText(list.get(position).dateTime);
//        }

        String url=list.get(position).imgUrl;
        Picasso.get().load(url).into(event_image);
        event_title.setText(list.get(position).name);
        event_category_location.setText(list.get(position).cat+"("+list.get(position).district+")");
//        event_location.setText(mDataset.get(position).location);
        event_people.setText(Integer.toString(list.get(position).people)+"/"+Integer.toString(list.get(position).maximum));
        event_happybi.setText(Integer.toString(list.get(position).reward)+"獎勵");
        if(list.get(position).dateTime==null){
            event_dateTime.setText("");
        }else{
            event_dateTime.setText(list.get(position).dateTime);
        }

        return convertView;
    }
}
