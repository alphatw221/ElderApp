package com.example.myapplication.AlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Model_Class.Location_class;
import com.example.myapplication.R;

public class LocationDetailAlertDialog extends AlertDialog implements View.OnClickListener {

    private Context mContext;
    private Button location_detail_link,location_detail_close;
    private TextView location_detail_text;
    private Location_class location_class;

    public LocationDetailAlertDialog(Context context,Location_class lc) {
        super(context);
        mContext=context;
        location_class=lc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail_alert_dialog);

        location_detail_close=(Button)findViewById(R.id._location_detail_close);
        location_detail_link=(Button)findViewById(R.id._location_detail_link);
        location_detail_text=(TextView)findViewById(R.id._location_detail_text);

        location_detail_text.setText("名稱:"+location_class.name+"\n"+"地址:"+location_class.address);

        location_detail_link.setOnClickListener(this);
        location_detail_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id._location_detail_link:
                Intent i =new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(location_class.link));
                mContext.startActivity(i);
                    this.dismiss();
                break;

            case R.id._location_detail_close:
                    this.dismiss();
                break;
        }
    }
}
