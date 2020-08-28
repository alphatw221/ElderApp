package com.elderApp.ElderApp.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class write_post_Frag extends Fragment {
    private ImageButton post_post_back_btn;
    private Button post_post_btn,post_post_uploadimg_btn,post_post_image_cancel;
    private Button upload_image_bottom_sheet_cancel,upload_image_bottom_sheet_capture,upload_image_bottom_sheet_select,upload_image_bottom_sheet_remove;

    private EditText post_post_title,post_post_body;
    private ImageView post_post_image;
    private Bitmap bitmap=null;
    private BottomSheetDialog bottomSheetDialog;

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

        bottomSheetDialog=new BottomSheetDialog(context);
        View view1=LayoutInflater.from(context).inflate(R.layout.upload_image_bottom_sheet_layout,null);
        upload_image_bottom_sheet_cancel=view1.findViewById(R.id._upload_image_bottom_sheet_cancel);
        upload_image_bottom_sheet_cancel.setOnClickListener(btn_listener);
        upload_image_bottom_sheet_capture=view1.findViewById(R.id._upload_image_bottom_sheet_capture);
        upload_image_bottom_sheet_capture.setOnClickListener(btn_listener);
        upload_image_bottom_sheet_select=view1.findViewById(R.id._upload_image_bottom_sheet_select);
        upload_image_bottom_sheet_select.setOnClickListener(btn_listener);
//        upload_image_bottom_sheet_remove=view1.findViewById(R.id._upload_image_bottom_sheet_remove);
//        upload_image_bottom_sheet_remove.setOnClickListener(btn_listener);
        bottomSheetDialog.setContentView(view1);
        ViewGroup parent = (ViewGroup) view1.getParent();
        parent.setBackgroundResource(android.R.color.transparent);
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
                    bottomSheetDialog.show();
                    break;
                case R.id._post_post_image_cancel:
                    post_post_image.setImageURI(null);
                    post_post_image.setVisibility(View.GONE);
                    post_post_image_cancel.setVisibility(View.GONE);
                    bitmap=null;
                    break;
                case R.id._upload_image_bottom_sheet_cancel:
                    bottomSheetDialog.cancel();
                    break;
                case R.id._upload_image_bottom_sheet_select:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,1);
                    bottomSheetDialog.cancel();
                    break;
                case R.id._upload_image_bottom_sheet_capture:
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},110);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) { }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(getContext(),
                                        "com.elderApp.ElderApp.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 2);
                            }
                        }
                    }
                    bottomSheetDialog.cancel();
                    break;
            }
        }
    };
    private String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName="JPEG";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:"+image.getAbsolutePath();
        return image;
    }

    private String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Uri path=data.getData();
//                    String destinationFileName = "IMG_" + System.currentTimeMillis();
                    String destinationFileName = "IMG_" ;
                    UCrop ucrop=UCrop.of(path, Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));
                    startActivityForResult(ucrop.getIntent(context),UCrop.REQUEST_CROP);
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    Uri path=Uri.parse(currentPhotoPath);
                    UCrop ucrop=UCrop.of(path, path);
                    startActivityForResult(ucrop.getIntent(context),UCrop.REQUEST_CROP);
                }
                break;
            case UCrop.REQUEST_CROP:
                if(resultCode==RESULT_OK){
                    Uri resultUri = UCrop.getOutput(data);
                    post_post_image.setImageURI(null);
                    post_post_image.setImageURI(resultUri);
                    post_post_image.setVisibility(View.VISIBLE);
                    post_post_image_cancel.setVisibility(View.VISIBLE);
                    try {
                        bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(resultUri));
                    }catch (IOException e){ }
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                break;
//        if(requestCode==2&&resultCode==RESULT_OK&&data!=null){
//            Uri path=data.getData();
//            post_post_image.setImageURI(null);
//            post_post_image.setImageURI(path);
//            post_post_image.setVisibility(View.VISIBLE);
//            post_post_image_cancel.setVisibility(View.VISIBLE);
//            try {
//                bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),path);
//            }catch (IOException e){ }}
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) { }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.elderApp.ElderApp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 2);
                    }
                }
            }
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
                            }else if(response.has("alert")){
                                new AlertDialog.Builder(context).setMessage(response.getString("alert")).show();
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