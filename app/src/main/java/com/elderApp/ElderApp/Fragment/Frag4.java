package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Helper_Class.post_listview_adapter;
import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Frag4 extends Fragment {

    private Context context;
    private Button post_last_page,post_next_page,post_show_btn,post_create_btn,post_mypost_btn;
    private ListView post_listview;
    private TextView post_page_status;
    private ProgressBar post_progressbar;
    private Integer page=1;
    private post_listview_adapter adapter;
    private JSONArray postList=new JSONArray();
    private String btn_flag="left";
    private Integer post_start=1;
    private Integer post_end,total;
    private JSONObject pagination;
    public Frag4() {
    }


    public void update() {
        if(btn_flag.equals("left")){
            show_all_post(page);
        }else{
            show_my_post(page);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag4_layout, container, false);
        context=this.getContext();
        post_last_page=view.findViewById(R.id._post_last_page);
        post_last_page.setOnClickListener(btn_listener);
        post_next_page=view.findViewById(R.id._post_next_page);
        post_next_page.setOnClickListener(btn_listener);
        post_show_btn=view.findViewById(R.id._post_show_btn);
        post_show_btn.setOnClickListener(btn_listener);
        post_mypost_btn=view.findViewById(R.id._post_mypost_btn);
        post_mypost_btn.setOnClickListener(btn_listener);
        post_create_btn=view.findViewById(R.id._post_create_btn);
        post_create_btn.setOnClickListener(btn_listener);

        post_listview=view.findViewById(R.id._post_listview);
        post_progressbar=view.findViewById(R.id._post_progressbar);

        post_page_status=view.findViewById(R.id._post_page_status);

        adapter=new post_listview_adapter(context,postList);
        post_listview.setAdapter(adapter);
        post_listview.setOnItemClickListener(listview_listener);
        show_all_post(1);
        return view;
    }

    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id._post_show_btn:
                    if(btn_flag.equals("right")){
                        post_show_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                        post_mypost_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        btn_flag="left";
                        show_all_post(1);
                    }
                    break;
                case (R.id._post_mypost_btn):
                    if(btn_flag.equals("left")){
                        post_mypost_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                        post_show_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        btn_flag="right";
                        show_my_post(1);
                    }
                    break;
                case R.id._post_next_page:
                    if(total>post_end){
                        page++;
                        try {
                            post_start=post_start+pagination.getInt("rows");
                        }catch (JSONException e){}
                        if(btn_flag.equals("left")){
                            show_all_post(page);
                        }else{
                            show_my_post(page);
                        }
                    }
                    break;
                case R.id._post_last_page:
                    if(post_start>1){
                        page--;
                        try {
                            post_start=post_start-pagination.getInt("rows");
                        }catch (JSONException e){}
                        if(btn_flag.equals("left")){
                            show_all_post(page);
                        }else{
                            show_my_post(page);
                        }
                    }
                    break;
                case R.id._post_create_btn:
                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("write_post_Frag");
                    Fragment fragment2=FM.findFragmentByTag("Frag4");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.hide(fragment2);

                        } else {
                            //                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                            FT.add(R.id._fragment_frag4_blank, fragment, "write_post_Frag");
                            FT.hide(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag4_blank,new write_post_Frag(),"write_post_Frag");
                    }
                    FT.commit();
                    break;

            }

        }
    };

    private ListView.OnItemClickListener listview_listener =new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String slug=new String();
            try{
                slug=postList.getJSONObject(position).getString("slug");
            }catch (JSONException e){}

            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("post_detail_Frag");
            Fragment fragment2=FM.findFragmentByTag("Frag4");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.hide(fragment2);

                } else {
                    //                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag4_blank, fragment, "post_detail_Frag");
                    FT.hide(fragment2);
                }
            } else{
                FT.add(R.id._fragment_frag4_blank,new post_detail_Frag(slug),"post_detail_Frag");
//                Fragment fragment3=FM.findFragmentByTag("post_detail_Frag");
//                FT.show(fragment3);
                FT.hide(fragment2);
//                FT.replace(R.id._fragment_frag4_blank,new post_detail_Frag(slug),"post_detail_Frag");
            }
            FT.commit();
        }
    };

    private void show_all_post(Integer page){
        post_progressbar.setVisibility(View.VISIBLE);
        String url="https://www.happybi.com.tw/api/post/list";
        JsonObjectRequest allPostRequest=new JsonObjectRequest(0, url + "?page=" + page+"&descending=true", null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    postList=response.getJSONArray("postList");
                    pagination=response.getJSONObject("pagination");
                    total=response.getInt("total");

                }catch (JSONException e){ }

                adapter.update(postList);
                post_progressbar.setVisibility(View.GONE);
                if(postList.length()==0){
                }else{
                    post_end=postList.length()+post_start-1;
                    post_page_status.setText(post_start.toString()+"~"+post_end.toString()+"/"+total.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(context)
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
    private void show_my_post(Integer page){
        post_progressbar.setVisibility(View.VISIBLE);
        String url="https://www.happybi.com.tw/api/post/myPostList";
        JsonObjectRequest allPostRequest=new JsonObjectRequest(0, url + "?page=" + page+"&descending=true", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            postList=response.getJSONArray("postList");
                            pagination=response.getJSONObject("pagination");
                            total=response.getInt("total");

                        }catch (JSONException e){ }
//                        adapter.notifyDataSetChanged();
                        adapter.update(postList);
                        post_progressbar.setVisibility(View.GONE);
                        if(postList.length()==0){
                        }else{
                            post_end=postList.length()+post_start-1;
                            post_page_status.setText(post_start.toString()+"~"+post_end.toString()+"/"+total.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(context)
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