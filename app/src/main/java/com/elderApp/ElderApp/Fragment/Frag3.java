package com.elderApp.ElderApp.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elderApp.ElderApp.Activity.MainActivity;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.Activity.WebViewActivity;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.AlertHandler;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Helper_Class.QRCodeHelper;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.R;
import com.elderApp.ElderApp.Activity.UpdateMyDataActivity;
import com.elderApp.ElderApp.Helper_Class.myJsonRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Frag3 extends Fragment {
    private EditText myAccount_name,myAccount_account,myAccount_gender,
            myAccount_birthday,myAccount_cellPhone,myAccount_tel,myAccount_address,myAccount_idcode;
    private Button myAccount_update,myAccount_apply,myAccount_agrement,myAccount_logout;
    private Button upload_image_bottom_sheet_cancel,upload_image_bottom_sheet_capture,upload_image_bottom_sheet_select,upload_image_bottom_sheet_remove;
    private TextView myAccount_member,myAccount_valid,myAccount_expiry_date;
    private ImageView myAccount_qr,myAccount_image;

    private String url2="https://app.happybi.com.tw/api/extendMemberShip";
    private SharedPreferences preferences;

    private Context context;
    private int user_id;
    private ImageButton myAccount_image_edit;
    private Bitmap bitmap;
    private BottomSheetDialog bottomSheetDialog;

    private String locationUrl;
    private Button locationUrlButton;

    private String myCourserUrl;
    private Button myCourseUrlButton;

    private static final int LINE_REQUEST_CODE = 11;
    private Button bindLineButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag3_layout,container,false);
        context=this.getContext();

        //------------抓取物件----------------------------------------------------------------------------------------------------------------------------
        myAccount_name=(EditText)view.findViewById(R.id._myAccount_name);
        myAccount_account=(EditText)view.findViewById(R.id._myAccount_account);
        myAccount_gender=(EditText)view.findViewById(R.id._myAccount_gender);
        myAccount_birthday=(EditText)view.findViewById(R.id._myAccount_birthday);
        myAccount_cellPhone=(EditText)view.findViewById(R.id._myAccount_cellPhone);
        myAccount_tel=(EditText)view.findViewById(R.id._myAccount_tel);
        myAccount_address=(EditText)view.findViewById(R.id._myAccount_address);
        myAccount_idcode=(EditText)view.findViewById(R.id._myAccount_idcode);
        myAccount_qr=(ImageView) view.findViewById(R.id._myAccount_qr);
        myAccount_update=(Button)view.findViewById(R.id._myAccount_update);
        myAccount_agrement=(Button)view.findViewById(R.id._myAccount_agrement);
        myAccount_logout=(Button)view.findViewById(R.id._myAccount_logout);
        myAccount_apply=(Button)view.findViewById(R.id._myAccount_apply);
        myAccount_valid=view.findViewById(R.id._myAccount_valid);
        myAccount_expiry_date=view.findViewById(R.id._myAccount_expiry_date);
        myAccount_image_edit=view.findViewById(R.id._myAccount_image_edit);
        myAccount_image=view.findViewById(R.id._myAccount_image);

        bottomSheetDialog = new BottomSheetDialog(context);
        View popupButtonView = LayoutInflater.from(context).inflate(R.layout.upload_image_bottom_sheet_layout,null);
        upload_image_bottom_sheet_cancel=popupButtonView.findViewById(R.id._upload_image_bottom_sheet_cancel);
        upload_image_bottom_sheet_cancel.setOnClickListener(btn_listener);
        upload_image_bottom_sheet_capture=popupButtonView.findViewById(R.id._upload_image_bottom_sheet_capture);
        upload_image_bottom_sheet_capture.setOnClickListener(btn_listener);
        upload_image_bottom_sheet_select=popupButtonView.findViewById(R.id._upload_image_bottom_sheet_select);
        upload_image_bottom_sheet_select.setOnClickListener(btn_listener);

        bottomSheetDialog.setContentView(popupButtonView);
        ViewGroup parent = (ViewGroup) popupButtonView.getParent();
        parent.setBackgroundResource(android.R.color.transparent);

        locationUrlButton = view.findViewById(R.id.locationUrlButton);
        myCourseUrlButton = view.findViewById(R.id.myCourseUrlButton);
        bindLineButton = view.findViewById(R.id.bindLineButton);

        //---------------------發出請求------------------------------------------------------------
        getMyAccount();
        //-------------初始設定---------------------------------------------------------------------------------------------------------------------------
        myAccount_logout.setOnClickListener(logout_listener);
        myAccount_agrement.setOnClickListener(agreement_listener);
        myAccount_update.setOnClickListener(update_listener);
        myAccount_apply.setOnClickListener(apply_listener);
        myAccount_image_edit.setOnClickListener(image_edit_listener);
        preferences=this.getActivity().getSharedPreferences("preFile",MODE_PRIVATE);

        //----------------------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }


    private void getMyAccount(){
        apiService.getMyAccountRequest(context, responseListener, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                new ios_style_alert_dialog_1
                        .Builder(getActivity())
                        .setTitle("連線錯誤")
                        .setMessage("請重新登入")
                        .show();
            }
        });
    }


    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener responseListener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try{
                myAccount_name.setText(response.getString("name"));
                myAccount_account.setText(response.getString("email"));
                if(response.getString("gender").equals("1")){
                    myAccount_gender.setText("男性");
                }else myAccount_gender.setText("女性");
                myAccount_birthday.setText(response.getString("birthdate"));
                myAccount_cellPhone.setText(response.getString("phone"));
                myAccount_tel.setText(response.getString("tel"));
                myAccount_address.setText(response.getString("address"));
                myAccount_idcode.setText(response.getString("id_number"));
                if(response.getString("expiry_date")==null){
                    myAccount_expiry_date.setText("---");
                }else{
                    myAccount_expiry_date.setText(response.getString("expiry_date"));
                }

                if(response.getInt("valid")==0){
                    myAccount_valid.setText("待付款");
                    myAccount_valid.setTextColor(Color.parseColor("#DF4B5A"));
                }else {
                    myAccount_valid.setText("有效");
                    myAccount_valid.setTextColor(Color.parseColor("#4AB566"));
                }
                if(response.getString("img")!=null){
                    Picasso.get().load(response.getString("img")).into(myAccount_image);
                }

                Bitmap bitmap = QRCodeHelper
                        .newInstance(context)
                        .setContent(response.getString("id_code"))
                        .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                        .setMargin(1)
                        .getQRCOde();
                myAccount_qr.setImageBitmap(bitmap);
                user_id=response.getInt("id");

                if(response.has("locationUrl")){
                    locationUrlButton.setVisibility(View.VISIBLE);
                    locationUrl = response.getString("locationUrl");
                    locationUrlButton.setOnClickListener(navigate_locationPanel);
                }
                if(response.has("myCourseUrl")){
                    myCourseUrlButton.setVisibility(View.VISIBLE);
                    myCourserUrl = response.getString("myCourseUrl");
                    myCourseUrlButton.setOnClickListener(navigate_myCoursePanel);
                }

                if(!response.getBoolean("isLineAccountBinded")){
                    bindLineButton.setVisibility(View.VISIBLE);
                    bindLineButton.setOnClickListener(bindLineAccount);
                }

            }catch(JSONException e){
                new ios_style_alert_dialog_1
                        .Builder(context)
                        .setMessage("JSON錯誤")
                        .show();
            }
        }
    };



    private Button.OnClickListener navigate_locationPanel = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url",apiService.host + locationUrl + "?token=" + TabActivity.user.access_token);
            startActivity(intent);
        }
    };

    private Button.OnClickListener navigate_myCoursePanel = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url",apiService.host + myCourserUrl + "?token=" + TabActivity.user.access_token);
            startActivity(intent);
        }
    };

    private Button.OnClickListener bindLineAccount = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                Intent loginIntent = LineLoginApi.getLoginIntent(context,MainActivity.line_channelId,new LineAuthenticationParams.Builder()
                        .scopes(Arrays.asList(Scope.PROFILE))
                        // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                        .build());
                startActivityForResult(loginIntent, LINE_REQUEST_CODE);
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        }
    };


    private Button.OnClickListener btn_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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
    //-----------------編輯相片Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener image_edit_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            bottomSheetDialog.show();
        }
    };

    //---------------------------------------------------------
    private String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    };

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
                    try {
                        bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(resultUri));
                    }catch (IOException e){ }
                    String url3="https://app.happybi.com.tw/api/auth/uploadImage";
            JSONObject jsonObject=new JSONObject();
            try{
                jsonObject.put("image",bitmap2String(bitmap));
            }catch (JSONException e){}
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1,url3,jsonObject,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try{
                        if(response.getString("status").equals("success")){
                            Picasso.get().load(response.getString("imgUrl")).resize(100,100).centerCrop().into(myAccount_image);
                        }else{
                            new ios_style_alert_dialog_1.Builder(context)
                                    .setMessage("連線失敗請重試")
                                    .show();
                        }
                    }catch (JSONException e){}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new ios_style_alert_dialog_1.Builder(context)
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
            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                break;
            case LINE_REQUEST_CODE:
                this.handleLineIntent(data);
                break;

        }
    }

    private void handleLineIntent(Intent data){
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {
            case SUCCESS:
                // Login successful
                String userId = result.getLineProfile().getUserId();
                bindLineRequest(userId);
                break;
            case CANCEL:
                // Login canceled by user
                System.out.println("canceled");
                break;
            default:
                // Login canceled due to other error
                System.out.println("error");
        }
    }

    private void bindLineRequest(String userId){
        apiService.bindLineAccountRequest(context, userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = "綁定失敗";
                if(response.trim().equals("success")){
                    result = "綁定成功";
                    bindLineButton.setVisibility(View.GONE);
                }
                AlertHandler.alert(context, "訊息", result, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("binding error");
            }
        });
    }


    //-----------------修改資料按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener update_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UpdateMyDataActivity.class);
            startActivity(intent);
//            getActivity().finish();
        }
    };

    //-----------------申請續會按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener apply_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("user_id",user_id);
            }catch (JSONException e){

            }

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(1, url2, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray myEventList=new JSONArray();
                    try {
                        new ios_style_alert_dialog_1.Builder(context).setMessage(response.getString("m")).show();
                    }catch (JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new ios_style_alert_dialog_1.Builder(context)
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

            MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);

        }
    };

    //-----------------服務條款按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener agreement_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };
    //-----------------登出按鈕Listener-----------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener logout_listener =new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            preferences.edit().remove("email").remove("password").remove("access_token").commit();
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };



}
