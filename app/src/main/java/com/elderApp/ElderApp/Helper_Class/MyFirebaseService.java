package com.elderApp.ElderApp.Helper_Class;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        System.out.println("fuck you fcm");
//        final Bundle bundle = remoteMessage.toIntent().getExtras();
//        if(bundle != null){
//            for (String key : bundle.keySet()) {
//                Object value = bundle.get(key);
//                if (value == null) {
//                    continue;
//                }
//                System.out.println(value.toString());
//            }
//        }

        if(remoteMessage.toIntent().getStringExtra("updateWallet") != null){
            System.out.println("Receive message updateWallet");
            Intent intent = new Intent("update-wallet");
            String message = remoteMessage.toIntent().getStringExtra("message");
            if(message != null){
                intent.putExtra("message",message);
            }
            Application app = new Application();
            LocalBroadcastManager.getInstance(app).sendBroadcast(intent);
        }

        //Log.d("tag", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d("tag", "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//
//            } else {
//
//            }
//
//        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("tag", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPreferences preference;
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        preference.edit().putString("pushtoken",token).commit();
        System.out.println("onNewToken");
    }
}
