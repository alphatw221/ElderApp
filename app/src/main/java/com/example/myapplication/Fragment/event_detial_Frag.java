package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class event_detial_Frag extends Fragment {
    private Event_class event_class;
    public event_detial_Frag(Event_class event_class) {
        this.event_class=event_class;
    }
    private ImageButton event_detail_back;
    private Button event_detail_join;
    private TextView event_detail_title,event_detail_time,event_detail_endtime,event_detail_body;
    private ImageView event_detail_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_event_detail,container,false);
        event_detail_back=view.findViewById(R.id._event_detail_back);
        event_detail_join=view.findViewById(R.id._event_detail_join);
        event_detail_image=view.findViewById(R.id._event_detail_image);
        event_detail_title=view.findViewById(R.id._event_detail_title);
        event_detail_time=view.findViewById(R.id._event_detail_time);
        event_detail_endtime=view.findViewById(R.id._event_detail_endtime);
        event_detail_body=view.findViewById(R.id._event_detail_body);


        String url="https://www.happybi.com.tw/images/events/"+event_class.slug+"/"+event_class.image;
        Picasso.get().load(url).into(event_detail_image);
        event_detail_title.setText(event_class.title);
        event_detail_time.setText(event_class.dateTime);
        event_detail_endtime.setText(event_class.deadline);
        event_detail_body.setText(event_class.body);

        event_detail_back.setOnClickListener(btn_listener);
        event_detail_join.setOnClickListener(btn_listener);
        return view;
    }
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id._event_detail_back:
                break;
                case R.id._event_detail_join:
                    break;

            }
        }
    };
}