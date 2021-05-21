package com.elderApp.ElderApp.AlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.elderApp.ElderApp.R;

public class CustomizedAlertDialog extends AlertDialog implements View.OnClickListener{


    protected CustomizedAlertDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail_alert_dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id._location_detail_link:

                break;

            case R.id._location_detail_close:
                this.dismiss();
                break;
        }
    }
}
