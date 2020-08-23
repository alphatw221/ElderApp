package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.Activity.getRewardActivity;
import com.elderApp.ElderApp.Activity.signupActivity;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class myEvent_detail_Frag extends Fragment {
    private Event_class event_class;

    public myEvent_detail_Frag(Event_class event_class) {
        this.event_class=event_class;
    }

    private ImageButton myevent_detail_back;
    private Button myevent_detail_cancel,myevent_detail_getreward,myevent_detail_signin;
    private TextView myevent_detail_title,myevent_detail_time,myevent_detail_endtime,myevent_detail_body,myevent_detail_reward;
    private ImageView myevent_detail_image;
    private String url="https://www.happybi.com.tw/api/event/eventDetail/";
    private String url2="https://www.happybi.com.tw/api/cancelevent/";
    private Context context;
    private boolean isParticipated;
    private WebView myevent_detail_webview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_event_detail_,container,false);
        context=this.getContext();
        myevent_detail_back=view.findViewById(R.id._myevent_detail_back);
        myevent_detail_cancel=view.findViewById(R.id._myevent_detail_cancel);
        myevent_detail_image=view.findViewById(R.id._myevent_detail_image);
        myevent_detail_title=view.findViewById(R.id._myevent_detail_title);
        myevent_detail_time=view.findViewById(R.id._myevent_detail_time);
        myevent_detail_endtime=view.findViewById(R.id._myevent_detail_endtime);
        myevent_detail_body=view.findViewById(R.id._myevent_detail_body);
        myevent_detail_reward=view.findViewById(R.id._myevent_detail_reward);
        myevent_detail_getreward=view.findViewById(R.id._myevent_detail_getreward);
        myevent_detail_signin=view.findViewById(R.id._myevent_detail_signup);
        myevent_detail_webview=view.findViewById(R.id._myevent_detail_webview);

        myevent_detail_back.setOnClickListener(btn_listener);
        myevent_detail_cancel.setOnClickListener(btn_listener);
        myevent_detail_getreward.setOnClickListener(btn_listener);
        myevent_detail_signin.setOnClickListener(btn_listener);

        JsonObjectRequest eventDetailRequest=new JsonObjectRequest(0, url+event_class.slug , null, RL,REL){
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
        MySingleton.getInstance(context).getRequestQueue().add(eventDetailRequest);
        return view;
    }

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            JSONObject event=new JSONObject();
            try {
                event=response.getJSONObject("event");
                isParticipated=response.getBoolean("isParticipated");

                Picasso.get().load(event.getString("imgUrl")).into(myevent_detail_image);
                myevent_detail_title.setText(event.getString("title"));
                if(event.getInt("type")==1){
                    myevent_detail_time.setText("活動時間:"+event.getString("dateTime"));
                    myevent_detail_endtime.setText("報名截止:"+event.getString("deadline"));
                }else{
                    myevent_detail_time.setVisibility(View.GONE);
                    myevent_detail_endtime.setVisibility(View.GONE);
                }
                myevent_detail_reward.setText(Integer.toString(event.getInt("reward"))+"獎勵");
//                myevent_detail_body.setText(event.getString("body"));
                myevent_detail_webview.loadData(event.getString("body"),"text/html","UTF-8");
            }catch (JSONException e){

            }
        }
    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(context)
                    .setTitle("連線錯誤")
                    .show();
            //
        }
    };



    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id._myevent_detail_back:
                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("Frag2");
                    Fragment fragment2=FM.findFragmentByTag("myevent_detail_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.remove(fragment2);

                        } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                            FT.add(R.id._fragment_frag2_blank, fragment, "Frag2");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag2_blank,new Frag2(),"Frag2");
                    }
                    FT.commit();

                    break;
                case R.id._myevent_detail_cancel:
                    JSONObject jsonObject=new JSONObject();
                    try {

                        jsonObject.put("id", TabActivity.user.user_id);
                    }catch (JSONException e){

                    }

                    JsonObjectRequest joinRequest=new JsonObjectRequest(1, url2 + event_class.slug, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray myEventList=new JSONArray();
                            try {
                                if(response.getInt("s")==1){
                                    new AlertDialog.Builder(context)
                                            .setTitle(response.getString("m"))
                                            .show();
                                }else{
                                    new AlertDialog.Builder(context)
                                            .setTitle("參加失敗")
                                            .setMessage(response.getString("m"))
                                            .show();
                                }


                            }catch (JSONException e){

                            }
//
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(context)
                                    .setTitle("取消失敗 請檢查連線")
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
                    MySingleton.getInstance(context).getRequestQueue().add(joinRequest);
                    break;
                case R.id._myevent_detail_getreward:
                    Intent i1 = new Intent(myEvent_detail_Frag.this.getContext(), getRewardActivity.class);
                    startActivity(i1);
                    break;
                case R.id._myevent_detail_signup:
                    Intent i2 = new Intent(myEvent_detail_Frag.this.getContext(),signupActivity.class);
//                    intent.setClass(context, signupActivity.class);
                    i2.putExtra("slug",event_class.slug);
                    i2.putExtra("name",event_class.name);
                    startActivity(i2);
                    break;
            }
        }
    };


}
