package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.Helper_Class.jasonList_2_objList;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.Model_Class.Product_class;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.event_listview_adapter;
import com.example.myapplication.Helper_Class.myJsonRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.List;

public class Frag2 extends Fragment implements MyAdapter.IMyOnClickListener{

    private ListView event_listview;

    private Context context;
    private event_listview_adapter eventlistviewadapter;
    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout event_search_layout;
    public List<Event_class> myDataset;
    String[]categories={"所有類別","歡樂旅遊","樂活才藝","健康課程","社會服務","天使培訓","長照據點","大型活動"};
    String[]districts={"所有地區","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
    private Spinner event_category_spinner;
    private Spinner event_district_spinner;
    private SearchView event_searchview;
    private boolean check_init=false;
    private boolean first_expand=false;
    private  MyAdapter.IMyOnClickListener THIS=this;


    public Frag2(List<Event_class> e) {
        this.myDataset=e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag2_layout,container,false);
        //---------------------發出請求------------------------------------------------------------

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
//        event_listview=(ListView)view.findViewById(R.id._event_listview);

        Button event_myevent_btn=(Button)view.findViewById(R.id._event_myevent_btn);
        Button event_search_btn=(Button)view.findViewById(R.id._event_search_btn);
        Button event_rollback_btn=(Button)view.findViewById(R.id._event_rollback_btn);
        event_search_layout=view.findViewById(R.id._event_search_layout);

        recyclerView=(RecyclerView)view.findViewById(R.id._event_listview);
        event_category_spinner=view.findViewById(R.id._event_category_spinner);
        event_district_spinner=view.findViewById(R.id._event_district_spinner);
        event_searchview=view.findViewById(R.id._event_searchview);

        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        context=this.getContext();
        event_myevent_btn.setOnClickListener(btn_listener);
        event_search_btn.setOnClickListener(btn_listener);
        event_rollback_btn.setOnClickListener(btn_listener);
        ArrayAdapter<String> adapter_category=new ArrayAdapter<String>(context,R.layout.my_spinner_item,categories);
        ArrayAdapter<String> adapter_district=new ArrayAdapter<String>(context,R.layout.my_spinner_item,districts);
        event_category_spinner.setAdapter(adapter_category);
        event_district_spinner.setAdapter(adapter_district);
        event_category_spinner.setOnItemSelectedListener(spinner_listener);

        event_district_spinner.setOnItemSelectedListener(spinner_listener);

        event_searchview.setOnQueryTextListener(searchview_listener);
        event_searchview.setOnClickListener(new SearchView.OnClickListener(){

            @Override
            public void onClick(View v) {
                event_searchview.onActionViewExpanded();
            }
        });
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(myDataset,THIS);
        recyclerView.setAdapter(mAdapter);
        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }


    //--------------spinner_listener-------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener spinner_listener=new Spinner.OnItemSelectedListener(){


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(check_init){
                mAdapter.filter(event_searchview.getQuery().toString(),cat_position2id(event_category_spinner.getSelectedItemPosition()) , event_district_spinner.getSelectedItemPosition());

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    //--------------searchview_listener-------------------------------------------------------------------------------
    private SearchView.OnQueryTextListener searchview_listener=new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(check_init){
                if(first_expand){
                    mAdapter.filter(event_searchview.getQuery().toString(),cat_position2id(event_category_spinner.getSelectedItemPosition()) , event_district_spinner.getSelectedItemPosition());
                }else first_expand=true;

            }
            return true;
        }
    };
    private int cat_position2id(int pos){
        int[] trans={0,5,6,7,9,10,11,12};
        return trans[pos];
    };


    //--------------btn_listener-------------------------------------------------------------------------------
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._event_search_btn:
                    event_search_layout.setVisibility(View.VISIBLE);
                    check_init=true;
                    break;
                case R.id._event_myevent_btn:
                    break;
                case R.id._event_rollback_btn:
                    event_search_layout.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

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


//-------------------實作myOnClick介面------------------------------------------------------------------------------
    @Override
    public void myOnClick(int position) {
        Event_class event_class=myDataset.get(position);
    }
}

