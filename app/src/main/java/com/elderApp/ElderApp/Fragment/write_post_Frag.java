package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class write_post_Frag extends Fragment {
    private ImageButton post_post_back_btn;
    private Button post_post_btn,post_post_uploadimg_btn,post_post_image_cancel;
    private EditText post_post_title,post_post_body;
    private ImageView post_post_image;
    private Bitmap bitmap=null;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_write_post_, container, false);
        context=this.getContext();
        post_post_back_btn=view.findViewById(R.id._post_post_back_btn);
        post_post_btn=view.findViewById(R.id._post_post_btn);
        post_post_title=view.findViewById(R.id._post_post_title);
        post_post_body=view.findViewById(R.id._post_post_body);
        post_post_uploadimg_btn=view.findViewById(R.id._post_post_uploadimg_btn);
        post_post_image_cancel=view.findViewById(R.id._post_post_image_cancel);
        post_post_image=view.findViewById(R.id._post_post_image);

        post_post_btn.setOnClickListener(btn_listener);
        post_post_btn.setOnClickListener(btn_listener);
        post_post_back_btn.setOnClickListener(btn_listener);
        post_post_image_cancel.setOnClickListener(btn_listener);
        post_post_uploadimg_btn.setOnClickListener(btn_listener);
        return view;
    }
    private Button.OnClickListener btn_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id._post_post_back_btn:

                    FragmentManager FM = getFragmentManager();
                    FragmentTransaction FT = FM.beginTransaction();
                    Fragment fragment=FM.findFragmentByTag("Frag4");
                    Fragment fragment2=FM.findFragmentByTag("write_post_Frag");
                    if ( fragment!=null) {
                        if ( fragment.isAdded()) {
                            FT.show(fragment);
                            FT.remove(fragment2);

                        } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                            FT.add(R.id._fragment_frag4_blank, fragment, "Frag4");
                            FT.remove(fragment2);
                        }
                    } else{
                        FT.replace(R.id._fragment_frag4_blank,new Frag4(),"Frag4");
                    }
                    FT.commit();
                    break;
                case R.id._post_post_btn:
                    upload_post();
                    break;
                case R.id._post_post_uploadimg_btn:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,2);
                    break;
                case R.id._post_post_image_cancel:
                    post_post_image.setImageURI(null);
                    post_post_image.setVisibility(View.GONE);
                    post_post_image_cancel.setVisibility(View.GONE);
                    bitmap=null;
                    break;
            }
        }
    };
    private String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            post_post_image.setImageURI(null);
            post_post_image.setImageURI(path);
            post_post_image.setVisibility(View.VISIBLE);
            post_post_image_cancel.setVisibility(View.VISIBLE);
            try {
                bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),path);
            }catch (IOException e){ }
        }
    }



    private void upload_post(){
        String url="https://www.happybi.com.tw/api/post/makeNewPost";
        JSONObject jsonObject=new JSONObject();
        try{
            if(bitmap!=null){
                jsonObject.put("image",bitmap2String(bitmap));
            }
            jsonObject.put("title",post_post_title.getText());
            jsonObject.put("body",post_post_body.getText());
        }catch (JSONException e){}
        JsonObjectRequest allPostRequest=new JsonObjectRequest(1, url , jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("m").equals("success")){
                                new AlertDialog.Builder(context).setMessage("發布成功").show();
                            }else{
                                new AlertDialog.Builder(context).setMessage(response.getString("m")).show();
                            }
                            FragmentManager FM = getFragmentManager();
                            FragmentTransaction FT = FM.beginTransaction();
                            Fragment fragment=FM.findFragmentByTag("Frag4");
                            Fragment fragment2=FM.findFragmentByTag("write_post_Frag");
                            if ( fragment!=null) {
                                if ( fragment.isAdded()) {
                                    FT.show(fragment);
                                    FT.remove(fragment2);

                                } else {
                                    FT.add(R.id._fragment_frag4_blank, fragment, "Frag4");
                                    FT.remove(fragment2);
                                }
                            } else{
                                FT.replace(R.id._fragment_frag4_blank,new Frag4(),"Frag4");
                            }
                            FT.commit();
                        }catch (JSONException e){ }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(context)
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
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

        MySingleton.getInstance(context).getRequestQueue().add(allPostRequest);
    }
}