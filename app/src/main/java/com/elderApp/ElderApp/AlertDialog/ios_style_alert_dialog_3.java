package com.elderApp.ElderApp.AlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.elderApp.ElderApp.R;

class ios_style_alert_dialog_3 extends AlertDialog {
    protected ios_style_alert_dialog_3(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_ios_style_3);
    }
}
