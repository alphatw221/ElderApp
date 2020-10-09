package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.comment_listview_adapter;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public class post_detail_Frag extends Fragment {

    private String slug;
    private ImageView post_detail_author_image,post_detail_image;
    private TextView post_detail_author,post_detail_dateTime,post_detail_title,post_detail_body,post_detail_like_number,post_detail_comment_number,post_detail_nocomment;
    private EditText post_detail_comment;
    private ImageButton post_detail_back ,post_detail_like_btn;
    private Button post_detail_comment_btn;
//    private ListView post_detail_listview;
    private LinearLayout post_detail_comment_list;
    private Context context;
    private JSONObject jsonObject=new JSONObject();
    private boolean isAuthor,hasLiked;
    private Integer likes,comments;
    private Integer page=1;
    private boolean hasNextPage;
    private JSONArray commentList=new JSONArray();
    private comment_listview_adapter adapter;
    private LayoutInflater myInflater;
    private ScrollView post_detail_scrollview;


    public post_detail_Frag(String slug) {
        this.slug=slug;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_post_detail_, container, false);
        context=this.getContext();
        myInflater=LayoutInflater.from(context);
        post_detail_author=view.findViewById(R.id._post_detail_author);

        post_detail_author_image=view.findViewById(R.id._post_detail_author_image);
        post_detail_image=view.findViewById(R.id._post_detail_image);
        post_detail_dateTime=view.findViewById(R.id._post_detail_dateTime);
        post_detail_title=view.findViewById(R.id._post_detail_title);
        post_detail_body=view.findViewById(R.id._post_detail_body);
        post_detail_like_number=view.findViewById(R.id._post_detail_like_number);
        post_detail_comment_number=view.findViewById(R.id._post_detail_comments_number);
        post_detail_comment=view.findViewById(R.id._post_detail_comment);
        post_detail_back=view.findViewById(R.id._post_detail_back);
        post_detail_like_btn=view.findViewById(R.id._post_detail_like_btn);
        post_detail_comment_btn=view.findViewById(R.id._post_detail_comment_btn);
        post_detail_comment_list=view.findViewById(R.id._post_detail_comment_list);
        post_detail_nocomment=view.findViewById(R.id._post_detail_nocomment);
        post_detail_scrollview=view.findViewById(R.id._post_detail_scrollview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            post_detail_scrollview.setOnScrollChangeListener(onScrollChangeListener);
        }else{
            new AlertDialog.Builder(context).setMessage("裝置版本過舊,留言板功能無法正常運行,建議更換裝置");
        }

        post_detail_comment_btn.setOnClickListener(btn_listener);
        post_detail_like_btn.setOnClickListener(btn_listener);
        post_detail_back.setOnClickListener(btn_listener);

        adapter=new comment_listview_adapter(commentList,context);

        show_post_detail();
        get_comments(page);
        return view;
    }

    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._post_detail_back:
                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("Frag4");
                    Frag4 f=(Frag4)FM.findFragmentByTag("Frag4");
                    Fragment fragment2=FM.findFragmentByTag("post_detail_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            f.update();
                            FT.remove(fragment2);

                        } else {
                            FT.add(R.id._fragment_frag4_blank, fragment, "Frag4");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag4_blank,new Frag4(),"Frag4");
                    }
                    FT.commit();
                    break;
                case R.id._post_detail_like_btn:
                    if(hasLiked){
                        //取消按讚
                        unlike_post();
                    }else{
                        //按讚
                        like_post();
                    }
                    break;
                case R.id._post_detail_comment_btn:
                    make_comment();
                    break;
            }
        }
    };

    private View.OnScrollChangeListener onScrollChangeListener=new View.OnScrollChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            View view=(View)post_detail_scrollview.getChildAt(post_detail_scrollview.getChildCount()-1);
            int bottomDetector=view.getBottom()-(post_detail_scrollview.getHeight()+post_detail_scrollview.getScrollY());
            if(bottomDetector<=0){
                if(hasNextPage){
                    post_detail_scrollview.setOnScrollChangeListener(null);
                    page++;
                    get_comments(page);
                }
            }
        }
    };

    private void show_post_detail(){
        String url="https://www.happybi.com.tw/api/post/detail/"+slug;
        JsonObjectRequest allPostRequest=new JsonObjectRequest(0, url , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonObject=response.getJSONObject("post");
                            isAuthor=response.getBoolean("isAuthor");
                            hasLiked=response.getBoolean("hasLiked");
                            String url=jsonObject.getString("user_image");
                            Picasso.get().load(url).resize(100,100).centerCrop().into(post_detail_author_image);
                            if(!jsonObject.isNull("post_image")&& !jsonObject.getString("post_image").equals("")){
                                post_detail_image.setVisibility(View.VISIBLE);
                                Picasso.get().load(jsonObject.getString("post_image")).into(post_detail_image);
                            }
                            post_detail_author.setText(jsonObject.getString("user_name"));
                            post_detail_title.setText(jsonObject.getString("title"));
                            post_detail_body.setText(jsonObject.getString("body"));
                            likes=jsonObject.getInt("likes");
                            post_detail_like_number.setText(Integer.toString(jsonObject.getInt("likes")));
                            comments=jsonObject.getInt("comments");
                            post_detail_comment_number.setText(Integer.toString(jsonObject.getInt("comments")));
                            post_detail_dateTime.setText(jsonObject.getString("created_at").substring(0,10));
                            if(hasLiked){
                                post_detail_like_btn.setImageResource(R.drawable.ic_heart_red);
                            }else{
                                post_detail_like_btn.setImageResource(R.drawable.ic_heart_stroke_white);
                            }
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }

    private void like_post(){
        String url="https://www.happybi.com.tw/api/post/likePost/"+slug;
        JsonObjectRequest allPostRequest=new JsonObjectRequest(1, url , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("m").equals("success")) {
                                post_detail_like_btn.setImageResource(R.drawable.ic_heart_red);
                                likes++;
                                post_detail_like_number.setText(Integer.toString(likes));
                                hasLiked=true;
                            }else if(response.has("alert")){
                                new ios_style_alert_dialog_1.Builder(context).setMessage(response.getString("alert")).show();
                            }
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }

    private void unlike_post(){
        String url="https://www.happybi.com.tw/api/post/unLikePost/"+slug;
        JsonObjectRequest allPostRequest=new JsonObjectRequest(1, url , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("m").equals("success")) {
                                post_detail_like_btn.setImageResource(R.drawable.ic_heart_stroke_white);
                                likes--;
                                post_detail_like_number.setText(Integer.toString(likes));
                                hasLiked=false;
                            }
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }

    private void get_comments(Integer page){
        String url="https://www.happybi.com.tw/api/post/commentList/"+slug+"?page="+page;
        JsonObjectRequest allPostRequest=new JsonObjectRequest(0, url , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hasNextPage=response.getBoolean("hasNextPage");
                            commentList=response.getJSONArray("commentList");
//                            adapter.update(commentList);
//                            setListViewHeightBasedOnChildren(post_detail_listview);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                inflate_comment_list();
                            }


                            if(commentList.length()==0){
                                post_detail_nocomment.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void inflate_comment_list(){
        adapter.update(commentList);
        for(int i=0;i<commentList.length();i++){
            post_detail_comment_list.addView(adapter.getView(i,null,null));
        }
        post_detail_scrollview.setOnScrollChangeListener(onScrollChangeListener);
    }

    private void insert_comment_list(View view){
        post_detail_comment_list.addView(view);
        comments++;
        post_detail_comment_number.setText(Integer.toString(comments));
    }

    private void make_comment(){
        String url="https://www.happybi.com.tw/api/post/commentOnPost/"+slug;
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("comment",post_detail_comment.getText().toString());
        }catch (JSONException e){}

        JsonObjectRequest allPostRequest=new JsonObjectRequest(1, url , jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject1=new JSONObject();

                        try {
                            if(response.has("alert")){
                                new ios_style_alert_dialog_1.Builder(context).setMessage(response.getString("alert")).show();
                                return;
                            }

                            jsonObject1=response.getJSONObject("comment");
                            commentList.put(jsonObject1);
                            if(!hasNextPage){
                                View view=myInflater.inflate(R.layout.comment_listview_layout,null);
                                Picasso.get().load(jsonObject1.getString("user_image")).resize(50,50).centerCrop().into((ImageView) view.findViewById(R.id._comment_author_image));
                                ((TextView)view.findViewById(R.id._comment_author)).setText(jsonObject1.getString("user_name"));
                                ((TextView)view.findViewById(R.id._comment_dateTime)).setText(jsonObject1.getString("created_at").substring(0,10));
                                ((TextView)view.findViewById(R.id._comment_text)).setText((jsonObject1.getString("body")));
                                insert_comment_list(view);
                                post_detail_comment.onEditorAction(EditorInfo.IME_ACTION_DONE);
                                post_detail_comment.setText("");
                                post_detail_nocomment.setVisibility(View.GONE);
                            }
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String Token=getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+Token);
                return headers;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }

}