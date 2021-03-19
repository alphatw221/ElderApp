package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.EventDetailActivity;
import com.elderApp.ElderApp.Activity.TakeMoneyActivity;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.Helper_Class.jasonList_2_objList;
import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.event_listview_adapter;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;

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

    private RecyclerView.LayoutManager layoutManager;

    private List<Event_class> eventList = new ArrayList<Event_class>();

    private Integer page = 1;
    private boolean hasNextPage = true;

    private Button event_myevent_btn, event_btn;
    private ProgressBar event_progressbar;

    private enum EventType{
        AllEvent,MyEvent
    }
    private EventType eventType = EventType.AllEvent;


    public Frag2() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag2_layout,container,false);
        context=this.getContext();

        //------------抓取物件---------------------------------------------------------------------
        event_listview=view.findViewById(R.id._event_listview);
        event_myevent_btn=(Button)view.findViewById(R.id._event_myevent_btn);
        event_btn=(Button)view.findViewById(R.id._event_btn);
        event_progressbar=view.findViewById(R.id._event_progressbar);

        //-------------初始設定--------------------------------------------------------------------
        eventlistviewadapter = new event_listview_adapter(context,eventList);
        event_listview.setAdapter(eventlistviewadapter);

        event_listview.setOnItemClickListener(onItemClickListener);
        event_listview.setOnScrollListener(onScrollListener);

        event_btn.setOnClickListener(btn_listener);
        event_myevent_btn.setOnClickListener(btn_listener);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getEventList();
            }
        }, 1500); // wait for 1.5 seconds

        return view;
    }


    private void getEventList(){
        System.out.println("get event list request");
        event_progressbar.setVisibility(View.VISIBLE);
        hasNextPage = false;
        switch (eventType){
            case AllEvent:
                apiService.getAllEventRequest(context,page,responseListener,responseErrorListener);
                break;
            case MyEvent:
                apiService.getMyEventRequest(context,page,responseListener,responseErrorListener);
                break;
        }
    }

    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            event_progressbar.setVisibility(View.GONE);

            JSONArray eventArray = new JSONArray();
            try {
                eventArray = response.getJSONArray("eventList");
                hasNextPage = response.getBoolean("hasNextPage");
            }catch (JSONException e){
                System.out.println("error");
                return;
            }

            eventList.addAll(jasonList_2_objList.eventList(eventArray));
            page++;
            eventlistviewadapter.notifyDataSetChanged();

        }
    };

    private Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            event_progressbar.setVisibility(View.GONE);
            new ios_style_alert_dialog_1
                    .Builder(context)
                    .setTitle("連線錯誤")
                    .setMessage("請重新登入")
                    .show();
        }
    };

    //--------------btn_listener-------------------------------------------------------------------------------
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._event_btn:
                    eventType = EventType.AllEvent;
                    event_btn.setOnClickListener(null);
                    event_myevent_btn.setOnClickListener(btn_listener);
                    event_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    event_myevent_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    page=1;
                    eventList.clear();
                    getEventList();
                    break;
                case R.id._event_myevent_btn:
                    eventType = EventType.MyEvent;
                    event_myevent_btn.setOnClickListener(null);
                    event_btn.setOnClickListener(btn_listener);
                    event_myevent_btn.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_orange));
                    event_btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    eventList.clear();
                    page=1;
                    getEventList();
                    break;
            }
        }
    };



    private ListView.OnItemClickListener onItemClickListener=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Event_class event = eventList.get(position);
            Intent intent = new Intent(getContext(), EventDetailActivity.class);
            intent.putExtra("slug",event.slug);
            startActivity(intent);

        }
    };



    private ListView.OnScrollListener onScrollListener = new ListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) { }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(totalItemCount == 0){return;}
            if(!hasNextPage){ return; }
            if(totalItemCount - visibleItemCount == firstVisibleItem){
                getEventList();
            }
        };
    };



}

