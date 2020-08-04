package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.Helper_Class.MySingleton;
import com.example.myapplication.Helper_Class.jasonList_2_objList;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.Model_Class.Order_class;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.event_listview_adapter;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Frag2 extends Fragment {
                                        //implements MyAdapter.IMyOnClickListener
    private ListView event_listview;
    private Context context;
    private event_listview_adapter eventlistviewadapter;
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
//    private LinearLayout event_search_layout;
    public List<Event_class> myDataset,myEvent;
//    String[]categories={"所有類別","歡樂旅遊","樂活才藝","健康課程","社會服務","天使培訓","長照據點","大型活動"};
//    String[]districts={"所有地區","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
//    private Spinner event_category_spinner  ,  getEvent_district_spinner;
//    private SearchView event_searchview;
    private boolean check_init=false;
    private boolean first_expand=false;
//    private  MyAdapter.IMyOnClickListener THIS=this;
    private String url="https://www.happybi.com.tw/api/event/eventList";
    private String url2="https://www.happybi.com.tw/api/event/myEventList";
    private Integer page=1;
    private boolean hasNextPage=true;
    private Button event_myevent_btn,event_btn;
    private String btn_flag;
    public Frag2() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag2_layout,container,false);
        //---------------------發出請求------------------------------------------------------------
        myJsonRequest.GET_Request.getJSON_object(url+"?page="+page,null,null,context,RL_JA,REL);
        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        event_listview=(ListView)view.findViewById(R.id._event_listview);
        event_listview=view.findViewById(R.id._event_listview);
        event_myevent_btn=(Button)view.findViewById(R.id._event_myevent_btn);
        event_btn=(Button)view.findViewById(R.id._event_btn);
//        Button event_search_btn=(Button)view.findViewById(R.id._event_search_btn);
//        Button event_rollback_btn=(Button)view.findViewById(R.id._event_rollback_btn);
//        event_search_layout=view.findViewById(R.id._event_search_layout);

//        recyclerView=(RecyclerView)view.findViewById(R.id._event_listview);
//        event_category_spinner=view.findViewById(R.id._event_category_spinner);
//        event_district_spinner=view.findViewById(R.id._event_district_spinner);
//        event_searchview=view.findViewById(R.id._event_searchview);

        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();

        event_btn.setOnClickListener(btn_listener);
        event_myevent_btn.setOnClickListener(btn_listener);
        btn_flag="left";

//        event_btn.setOnClickListener(btn_listener);
//        event_rollback_btn.setOnClickListener(btn_listener);

//        ArrayAdapter<String> adapter_category=new ArrayAdapter<String>(context,R.layout.my_spinner_item,categories);
//        ArrayAdapter<String> adapter_district=new ArrayAdapter<String>(context,R.layout.my_spinner_item,districts);

//        event_category_spinner.setAdapter(adapter_category);
//        event_district_spinner.setAdapter(adapter_district);

//        event_category_spinner.setOnItemSelectedListener(spinner_listener);
//        event_district_spinner.setOnItemSelectedListener(spinner_listener);
//
//        event_searchview.setOnQueryTextListener(searchview_listener);
//        event_searchview.setOnClickListener(new SearchView.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                event_searchview.onActionViewExpanded();
//            }
//        });
        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }
    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL_JA=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            JSONArray eventList=new JSONArray();
            try {
                eventList=response.getJSONArray("eventList");
                hasNextPage=response.getBoolean("hasNextPage");
            }catch (JSONException e){

            }
            myDataset= jasonList_2_objList.convert_2_Event_list(context,eventList);
            eventlistviewadapter=new event_listview_adapter(context,myDataset);
            event_listview.setAdapter(eventlistviewadapter);
            event_listview.setOnScrollListener(onScrollListener);
            event_listview.setOnItemClickListener(onItemClickListener);
//            recyclerView.setHasFixedSize(false);
//            layoutManager = new LinearLayoutManager(context);
//            recyclerView.setLayoutManager(layoutManager);
//            mAdapter = new MyAdapter(myDataset,THIS);
//            recyclerView.setAdapter(mAdapter);
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(context)
                    .setTitle("連線錯誤")
                    .setMessage("請重新登入")
                    .show();
            //
        }
    };

    //--------------btn_listener-------------------------------------------------------------------------------
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._event_btn:
                    event_btn.setOnClickListener(null);
                    event_myevent_btn.setOnClickListener(btn_listener);
                    event_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    event_myevent_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    page=1;
                    btn_flag="left";

                    JsonObjectRequest allEventRequest=new JsonObjectRequest(0, url + "?page=" + page, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray myEventList=new JSONArray();
                            try {
                                myEventList=response.getJSONArray("eventList");
                                hasNextPage=response.getBoolean("hasNextPage");

                            }catch (JSONException e){

                            }
                            List<Event_class> temp= jasonList_2_objList.convert_2_Event_list(context,myEventList);
                            myDataset.clear();
                            myDataset.addAll(temp);
                            eventlistviewadapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(context)
                                    .setTitle("連線錯誤")
                                    .setMessage("請重新登入")
                                    .show();
                        }
                    });
                    MySingleton.getInstance(context).getRequestQueue().add(allEventRequest);
//                    event_search_layout.setVisibility(View.VISIBLE);
//                    check_init=true;
                    break;
                case R.id._event_myevent_btn:
                    event_myevent_btn.setOnClickListener(null);
                    event_btn.setOnClickListener(btn_listener);
                    event_myevent_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    event_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    page=1;
                    btn_flag="right";

                    JsonObjectRequest myEventRequest=new JsonObjectRequest(0, url2 + "?page=" + page, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray myEventList=new JSONArray();
                            try {
                                myEventList=response.getJSONArray("eventList");
                                hasNextPage=response.getBoolean("hasNextPage");

                            }catch (JSONException e){

                            }
//
                            List<Event_class> temp= jasonList_2_objList.convert_2_Event_list(context,myEventList);
                            myDataset.clear();
                            myDataset.addAll(temp);
                            eventlistviewadapter.notifyDataSetChanged();

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
                    MySingleton.getInstance(context).getRequestQueue().add(myEventRequest);

                    break;
//                case R.id._event_rollback_btn:
//                    event_search_layout.setVisibility(View.INVISIBLE);
//                    break;
            }
        }
    };

    private ListView.OnScrollListener onScrollListener=new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if(hasNextPage){

                if(firstVisibleItem>(totalItemCount-5)){
                    if(btn_flag.equals("left")){
                        page++;
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0, url + "?page=" + page, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray myEventList=new JSONArray();
                                try {
                                    myEventList=response.getJSONArray("eventList");
                                    hasNextPage=response.getBoolean("hasNextPage");

                                }catch (JSONException e){

                                }
                                List<Event_class> temp= jasonList_2_objList.convert_2_Event_list(context,myEventList);
                                myDataset.addAll(temp);
                                eventlistviewadapter.notifyDataSetChanged();
                                event_listview.setOnScrollListener(onScrollListener);
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
                                return headers;
                            }

                        };
                        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
                        event_listview.setOnScrollListener(null);
                    }else{
                        page++;
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(0,url2+"?page="+page,null,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONArray jsonArray=new JSONArray();
                                try {
                                    jsonArray=response.getJSONArray("eventList");
                                    hasNextPage=response.getBoolean("hasNextPage");
                                }catch (JSONException e){
                                }

                                List<Event_class> temp= jasonList_2_objList.convert_2_Event_list(context,jsonArray);
                                myDataset.addAll(temp);
                                eventlistviewadapter.notifyDataSetChanged();
                                event_listview.setOnScrollListener(onScrollListener);
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
                        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
                        event_listview.setOnScrollListener(null);
                    }

                }
            }

        };
    };

    private ListView.OnItemClickListener onItemClickListener=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (btn_flag.equals("left")){
                Event_class event_class=myDataset.get(position);

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                Fragment fragment=FM.findFragmentByTag("event_detail_Frag");
                Fragment fragment2=FM.findFragmentByTag("Frag2");
                if ( fragment!=null) {
                    if ( fragment.isAdded()) {
                        FT.show(fragment);
                        FT.hide(fragment2);

                    } else {
        //                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                        FT.add(R.id._fragment_frag2_blank, fragment, "event_detail_Frag");
                        FT.hide(fragment2);
                    }
                } else{
                    FT.replace(R.id._fragment_frag2_blank,new event_detial_Frag(event_class),"event_detail_Frag");
                }
                FT.commit();
            }else{
                Event_class event_class=myDataset.get(position);

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                Fragment fragment=FM.findFragmentByTag("myevent_detail_Frag");
                Fragment fragment2=FM.findFragmentByTag("Frag2");
                if ( fragment!=null) {
                    if ( fragment.isAdded()) {
                        FT.show(fragment);
                        FT.hide(fragment2);

                    } else {
        //                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                        FT.add(R.id._fragment_frag2_blank, fragment, "myevent_detail_Frag");
                        FT.hide(fragment2);
                    }
                } else{
                    FT.replace(R.id._fragment_frag2_blank,new myEvent_detail_Frag(event_class),"myevent_detail_Frag");
                }
                FT.commit();
            }
        }
    };

    //-------------------實作myOnClick介面------------------------------------------------------------------------------
//    @Override
//    public void myOnClick(int position) {
//        Event_class event_class=myDataset.get(position);
//
//        FragmentManager FM = getFragmentManager();
//        FragmentTransaction FT = FM.beginTransaction();
//        Fragment fragment=FM.findFragmentByTag("event_detail_Frag");
//        Fragment fragment2=FM.findFragmentByTag("Frag2");
//        if ( fragment!=null) {
//            if ( fragment.isAdded()) {
//                FT.show(fragment);
//                FT.hide(fragment2);
//
//            } else {
////                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
//                FT.add(R.id._fragment_frag2_blank, fragment, "event_detail_Frag");
//                FT.hide(fragment2);
//            }
//        } else{
//            FT.replace(R.id._fragment_frag2_blank,new event_detial_Frag(event_class),"event_detail_Frag");
//        }
//        FT.commit();
//
//    }


//    //--------------spinner_listener-------------------------------------------------------------------------------
//    private Spinner.OnItemSelectedListener spinner_listener=new Spinner.OnItemSelectedListener(){
//
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            if(check_init){
//                mAdapter.filter(event_searchview.getQuery().toString(),cat_position2id(event_category_spinner.getSelectedItemPosition()) , event_district_spinner.getSelectedItemPosition());
//
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };
//
//
//    //--------------searchview_listener-------------------------------------------------------------------------------
//    private SearchView.OnQueryTextListener searchview_listener=new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            if(check_init){
//                if(first_expand){
//                    mAdapter.filter(event_searchview.getQuery().toString(),cat_position2id(event_category_spinner.getSelectedItemPosition()) , event_district_spinner.getSelectedItemPosition());
//                }else first_expand=true;
//
//            }
//            return true;
//        }
//    };
//    private int cat_position2id(int pos){
//        int[] trans={0,5,6,7,9,10,11,12};
//        return trans[pos];
//    };
//



//    //---------------------回報Listener------------------------------------------------------------
//    private  Response.Listener RL_JA=new Response.Listener<JSONArray>(){
//        @Override
//        public void onResponse(JSONArray response) {
//
//            myDataset= jasonList_2_objList.convert_2_Event_list(context,response);
//            // specify an adapter (see also next example)
//            mAdapter = new MyAdapter(myDataset,THIS);
//            recyclerView.setAdapter(mAdapter);
//
//        }
//    };
//    //---------------------錯誤回報Listener------------------------------------------------------------
//    private Response.ErrorListener REL=new Response.ErrorListener(){
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("錯誤")
//                    .setIcon(R.mipmap.ic_launcher)
//                    .setMessage(error.toString())
//                    .show();
//        }
//    };



}

