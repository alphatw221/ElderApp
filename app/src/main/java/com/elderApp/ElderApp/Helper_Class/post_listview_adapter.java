package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.Model_Class.Post_class;
import com.elderApp.ElderApp.R;
import com.google.zxing.common.StringUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class post_listview_adapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private JSONArray postList;
    private Context context ;
    private ImageView post_author_image;
    private TextView post_author,post_dateTime,post_title,post_body,post_like_number,post_comment_number;
    private ImageView post_image;

    public post_listview_adapter(Context context,JSONArray postList) {
        myInflater=LayoutInflater.from(context);
        this.postList = postList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postList.length();
    }

    @Override
    public Object getItem(int position) {
        Object object=new Object();
        try{
            object= postList.get(position);
        }catch (JSONException e){}
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=myInflater.inflate(R.layout.post_listview_layout,null);
        post_author_image=convertView.findViewById(R.id._post_author_image);
        post_author=convertView.findViewById(R.id._post_author);
        post_dateTime=convertView.findViewById(R.id._post_dateTime);
        post_title=convertView.findViewById(R.id._post_title);
        post_body=convertView.findViewById(R.id._post_body);
        post_like_number=convertView.findViewById(R.id._post_like_number);
        post_comment_number=convertView.findViewById(R.id._post_comment_number);
        post_image=convertView.findViewById(R.id._post_image);

        try{
            JSONObject jsonObject=postList.getJSONObject(position);
            String url=jsonObject.getString("user_image");
            if(!url.isEmpty()){
                Picasso.get().load(url).resize(100,100).centerCrop().into(post_author_image);
            }
            if(!jsonObject.isNull("post_image")){
                Picasso.get().load(jsonObject.getString("post_image")).resize(250,200).centerCrop().into(post_image);
                post_image.setVisibility(View.VISIBLE);
            }
            post_author.setText(jsonObject.getString("user_name"));
            post_dateTime.setText(jsonObject.getString("created_at").substring(0,10));

            post_title.setText(jsonObject.getString("title"));
            post_body.setText(jsonObject.getString("body"));
            post_like_number.setText(Integer.toString(jsonObject.getInt("likes")));
            post_comment_number.setText(Integer.toString(jsonObject.getInt("comments")));

        }catch (JSONException e){}
        return convertView;
    }
    public void update(JSONArray postList){
        this.postList=postList;
        notifyDataSetChanged();
    }
}
