package com.elderApp.ElderApp.Helper_Class;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;

public class AlertHandler {

    public static void alert(Context context,String title,String message, DialogInterface.OnClickListener listener){
        new ios_style_alert_dialog_1.Builder(context).setTitle(title).setMessage(message).setPositiveButton("確定", listener).show();
    }

}
