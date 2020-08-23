package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elderApp.ElderApp.Model_Class.Comment_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class comment_listview_adapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private JSONArray commentList;
    private Context context ;
    private ImageView comment_author_image;
    private TextView comment_author,comment_dateTime,comment_text;

    public comment_listview_adapter(JSONArray commentList, Context context) {
        myInflater=LayoutInflater.from(context);
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentList.length();
    }

    @Override
    public Object getItem(int position) {
        Object object=new Object();
        try {
            object=(Object)commentList.getJSONObject(position);
        }catch(JSONException e){};
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=myInflater.inflate(R.layout.comment_listview_layout,null);
        try{
            JSONObject jsonObject=commentList.getJSONObject(position);
            comment_author_image=convertView.findViewById(R.id._comment_author_image);
            Picasso.get().load(jsonObject.getString("user_image")).resize(50,50).centerCrop().into(comment_author_image);
            comment_author=convertView.findViewById(R.id._comment_author);
            comment_author.setText(jsonObject.getString("user_name"));
            comment_dateTime=convertView.findViewById(R.id._comment_dateTime);
            comment_dateTime.setText(jsonObject.getString("created_at").substring(0,10));
            comment_text=convertView.findViewById(R.id._comment_text);
            comment_text.setText(jsonObject.getString("body"));
        }catch (JSONException e){}
        return convertView;
    }

    public void update(JSONArray commentList){
//        try{
//            for (int i = 0; i < commentList.length(); i++) {
//                this.commentList.put(commentList.getJSONObject(i));
//            }
//        }catch(JSONException e){}
        this.commentList=commentList;
//        notifyDataSetChanged();
    }
}
