package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    //-----------------全域變數-----------------------------------------------------------------------------------------------------------------------
    private SharedPreferences preference;
    private User user;
    private Context context;

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preference=getSharedPreferences("preFile",MODE_PRIVATE);


        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {
                //------------檢查版本----------------------------------------------------------------------------------------------------------------------------
                //------------執行通知設定----------------------------------------------------------------------------------------------------------------------------
                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener() {
//                            @Override
//                            public void onComplete(@NonNull Task task) {
//                                if (!task.isSuccessful()) {
//                                    return;
//                                }
//                                if( task.getResult() == null)
//                                    return;
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//                                // Log and toast
//                                Log.i("MainActivity","token "+token);
//                            }
//                        })
                ;


//                Intent Not_intent = new Intent(this,AlertDetails.class);
//                Not_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, Not_intent, 0);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.coin)
//                        .setContentTitle("testTitle")
//                        .setContentText("testContent")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                        .setCategory(NotificationCompat.CATEGORY_EVENT)
//                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true)
//                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setAutoCancel(true);
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                // notificationId is a unique int for each notification that you must define
//                notificationManager.notify(notificationId, builder.build());
                //------------檢查token----------------------------------------------------------------------------------------------------------------------------
                try{
                    if(preference.contains("access_token")){
                        check_token_expire();
                    }else{
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){

                }

            }

        }, 2*1000); // wait for 2 seconds



    }
    //------------通知頻道函式(26+)----------------------------------------------------------------------------------------------------------------------------
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "testname";
//            String description = "testdescripton";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
    //----------------------------------------------------------------------------------------------------------------------------------------

    //---------------------發出請求函式------------------------------------------------------------
    private boolean check_token_expire(){

        String url="https://www.happybi.com.tw/api/auth/me";
        Object[] key=new Object[]{"token"};
        Object[] value=new Object[]{getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token","")};
        myJsonRequest.POST_Request.getJSON_object(url,key,value,this.getApplicationContext(),RL,REL);
        return true;

    };
    //----------------------------------------------------------------------------------------------------------------------------------------

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                user=new User();
                user.user_id=response.getInt("user_id");
                user.id_code=response.getString("id_code");
                user.email=response.getString("email");
                user.name=response.getString("name");
                user.wallet= response.getInt("wallet");
                user.rank=response.getInt("rank");
                user.org_rank=response.getInt("org_rank");
            }catch (JSONException e)
            {

            }


//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, myAccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent.putExtra("User",user);
//
//            Intent intent1=new Intent();
//            intent1.setClass(MainActivity.this, EventActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent1.putExtra("User",user);

//            Intent intent2=new Intent();
//            intent2.setClass(MainActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent2.putExtra("User",user);

//            startActivity(intent);
//            startActivity(intent1);
//            startActivity(intent2);

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TabActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("User",user);
            startActivity(intent);
            finish();
        }
    };
    //----------------------------------------------------------------------------------------------------------------------------------------

    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            //
        }
    };
    //----------------------------------------------------------------------------------------------------------------------------------------

}

