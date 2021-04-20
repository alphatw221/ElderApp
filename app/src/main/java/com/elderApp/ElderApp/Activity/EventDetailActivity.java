package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.AlertHandler;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailActivity extends AppCompatActivity {

    private Context context;
    private String slug;
    private String name;
    private boolean isParticipated;
    private ImageButton myevent_detail_back;
    private Button myevent_detail_cancel,myevent_detail_join,myevent_detail_getreward,myevent_detail_signin;
    private TextView myevent_detail_title,myevent_detail_time,myevent_detail_endtime,myevent_detail_body,myevent_detail_reward;
    private ImageView myevent_detail_image;
    private WebView myevent_detail_webview;
    private Button share_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;


        Intent intent = getIntent();

        if(intent.getAction() != null){
            // ATTENTION: This was auto-generated to handle app links.
            String appLinkAction = intent.getAction();
            Uri appLinkData = intent.getData();
            String path = appLinkData.getPath();
            System.out.println("App link test");
            System.out.println(path);

            String[] str = path.split("/");
            if(str.length >= 4){
                slug = str[3];
            }
        }else{
            slug = intent.getStringExtra("slug");
        }


        setContentView(R.layout.fragment_my_event_detail_);

        myevent_detail_back = findViewById(R.id._myevent_detail_back);
        myevent_detail_cancel = findViewById(R.id._myevent_detail_cancel);
        myevent_detail_join = findViewById(R.id._myevent_detail_join);
        myevent_detail_image = findViewById(R.id._myevent_detail_image);
        myevent_detail_title = findViewById(R.id._myevent_detail_title);
        myevent_detail_time = findViewById(R.id._myevent_detail_time);
        myevent_detail_endtime = findViewById(R.id._myevent_detail_endtime);

        myevent_detail_reward = findViewById(R.id._myevent_detail_reward);
        myevent_detail_getreward = findViewById(R.id._myevent_detail_getreward);
        myevent_detail_signin = findViewById(R.id._myevent_detail_signup);
        myevent_detail_webview = findViewById(R.id._myevent_detail_webview);
        share_button = findViewById(R.id.share_button);

        myevent_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myevent_detail_cancel.setOnClickListener(clickCancel);
        myevent_detail_join.setOnClickListener(clickJoin);
        myevent_detail_getreward.setOnClickListener(clickGetReward);
        myevent_detail_signin.setOnClickListener(clickArrive);
        share_button.setOnClickListener(shareListener);


        getEventDetail();
    }

    private void showError(){
        new ios_style_alert_dialog_1
                .Builder(this)
                .setTitle("連線錯誤")
                .show();
    }
    private void errorExit(){
        showError();
        finish();
    }

    private void getEventDetail(){
        apiService.getEventDetailRequest(this, slug, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("get event detail error");
                errorExit();
            }
        });
    }

    private  Response.Listener responseListener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {

            try {
                JSONObject event = response.getJSONObject("event");
                isParticipated = response.getBoolean("isParticipated");

                Picasso.get().load(event.getString("imgUrl")).into(myevent_detail_image);

                name = event.getString("title");
                myevent_detail_title.setText(name);

                if(event.getInt("type")==1){
                    myevent_detail_time.setText("活動時間:"+event.getString("dateTime"));
                    myevent_detail_endtime.setText("報名截止:"+event.getString("deadline"));
                }else{
                    myevent_detail_time.setVisibility(View.GONE);
                    myevent_detail_endtime.setVisibility(View.GONE);
                }
                myevent_detail_reward.setText(Integer.toString(event.getInt("reward"))+"獎勵");
//                String html = "<body style='margin:0;padding:0'>" + event.getString("body") + "</body>";

                myevent_detail_webview.loadData(event.getString("body"),"text/html","UTF-8");

            }catch (JSONException e){
                errorExit();
            }

            isParticipated(isParticipated);
        }
    };

    private View.OnClickListener clickGetReward = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,ScannerActivity.class);
            intent.putExtra("scanType",ScannerActivity.ScanType.Reward);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickArrive = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            apiService.isUserArriveRequest(context, slug, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    int s = response.optInt("s");
                    if (s == 1) {
                        Intent intent = new Intent(context,PassportActivity.class);
                        intent.putExtra("name",name);
                        startActivity(intent);
                    } else if (s == 2) {
                        Intent intent = new Intent(context,ScannerActivity.class);
                        intent.putExtra("scanType",ScannerActivity.ScanType.Arrive);
                        startActivity(intent);
                    }else{
                        ErrorHandler.alert(context,"錯誤",response.optString("m"));
                    }
                }
            },ErrorHandler.defaultListener(context));
        }
    };

    private View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            apiService.cancelEventRequest(context, slug, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.optInt("s") == 1){
                        isParticipated(false);
                    }else{
                        String message = response.optString("m");
                        ErrorHandler.alert(context,"訊息",message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError();
                }
            });
        }
    };

    private View.OnClickListener clickJoin = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            apiService.joinEventRequest(context, slug, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.optInt("s") == 1){
                        isParticipated(true);
                    }else{
                        String message = response.optString("m");
                        ErrorHandler.alert(context,"訊息",message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError();
                }
            });
        }
    };

    private Button.OnClickListener shareListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String urlString = apiService.host + "/app/event/" + slug;
            sendIntent.putExtra(Intent.EXTRA_TEXT, urlString);
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    };

    private void isParticipated(boolean isParticipated){

        if(isParticipated){
            myevent_detail_cancel.setVisibility(View.VISIBLE);
            myevent_detail_join.setVisibility(View.GONE);
            myevent_detail_getreward.setVisibility(View.VISIBLE);
            myevent_detail_signin.setVisibility(View.VISIBLE);
            return;
        }

        myevent_detail_cancel.setVisibility(View.GONE);
        myevent_detail_join.setVisibility(View.VISIBLE);
        myevent_detail_getreward.setVisibility(View.GONE);
        myevent_detail_signin.setVisibility(View.GONE);
    }


}