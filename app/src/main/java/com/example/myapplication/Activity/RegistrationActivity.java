package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.Helper_Class.myJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    //-------------------宣告全域變數----------------------------------------------------------------------------------------------------------------------------------
    private Spinner District_Spinner,how2Pay_Spinner;
    private EditText account,PW,PWcomfirm,name,cellPhone,phone,idCode,address;
    private TextView account_error,PW_error,PWcomfirm_error,
            name_error,cellPhone_error,gender_error,idCode_error,
            district_error,address_error,how2Pay_error;
    private RadioButton male,female;
    private RadioGroup genderGroup;
    private Button go_back,submit;
    private DatePicker birthday_datePicker;
    String[]test={"請選擇地區","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
    String[] how2Pay={"請選擇付款方式","推薦人代收","自行繳費"};
    private SharedPreferences preference;
    private Context context;
    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //-------------------開始抓取物件----------------------------------------------------------------------------------------------------------------------------------
        go_back=(Button) findViewById(R.id._return);
        submit=(Button)findViewById(R.id._subbmit);

        genderGroup=(RadioGroup) findViewById(R.id._genderGroup);
        male=(RadioButton) findViewById(R.id._male);
        female=(RadioButton) findViewById(R.id._female);

        District_Spinner=(Spinner) findViewById(R.id._district);
        how2Pay_Spinner=(Spinner)findViewById(R.id._how2pay);
        birthday_datePicker=(DatePicker) findViewById(R.id._birthday);

        account=(EditText) findViewById(R.id._account);
        PW=(EditText)findViewById(R.id._PW);
        PWcomfirm=(EditText)findViewById(R.id._PWcomfirm);
        name=(EditText)findViewById(R.id._person_name);
        cellPhone=(EditText)findViewById(R.id._cellPhone);
        phone=(EditText)findViewById(R.id._phone);
        idCode=(EditText)findViewById(R.id._idCode);
        address=(EditText)findViewById(R.id._address);

        account_error=(TextView)findViewById(R.id._account_error);
        PW_error=(TextView)findViewById(R.id._PW_error);
        PWcomfirm_error=(TextView)findViewById(R.id._PWcomfirm_error);
        name_error=(TextView)findViewById(R.id._name_error);
        cellPhone_error=(TextView)findViewById(R.id._cellPhone_error);
        gender_error=(TextView)findViewById(R.id._gender_error);
        idCode_error=(TextView)findViewById(R.id._idCode_error);
        district_error=(TextView)findViewById(R.id._district_error);
        address_error=(TextView)findViewById(R.id._address_error);
        how2Pay_error=(TextView)findViewById(R.id._how2pay_error);
        //--------------------初始設定---------------------------------------------------------------------------------------------------------------------------------
        ArrayAdapter<String> adapterTest=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,test);
        ArrayAdapter<String> adapter_how2Pay=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,how2Pay);

        adapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_how2Pay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        District_Spinner.setAdapter(adapterTest);
        how2Pay_Spinner.setAdapter(adapter_how2Pay);
        District_Spinner.setOnItemSelectedListener(DistrictListener);
        how2Pay_Spinner.setOnItemSelectedListener(how2PayListener);

        go_back.setOnClickListener(go_backListener);
        submit.setOnClickListener(submitListener);
        preference=getSharedPreferences("preFile",MODE_PRIVATE);
        context=this.getApplicationContext();
    }

//-------------------------------地區下拉Listener----------------------------------------------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener DistrictListener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String _district=parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
//-----------------------------付款方式下拉Listener------------------------------------------------------------------------------------------------------------------------
    private Spinner.OnItemSelectedListener how2PayListener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String _how2Pay=parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
//-----------------------------送出按鈕Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener submitListener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(verification()){
                //--------------------制定參數------------------------------------------------------------
                String url = "https://www.happybi.com.tw/api/auth/signup";
                int gender_int;
                if(male.isChecked())gender_int=1;else gender_int=0;
                String birthday_Date=Integer.toString(birthday_datePicker.getYear()) + "-" + Integer.toString(birthday_datePicker.getMonth()) + "-" + Integer.toString(birthday_datePicker.getDayOfMonth());
                Object[] key=new Object[]{"id_code","name","email","password","gender","birthday","phone","tel","address","district_id","district_name"};
                Object[] value=new Object[]{idCode.getText().toString(),name.getText().toString(),account.getText().toString(),
                        PW.getText().toString(),gender_int,birthday_Date,cellPhone.getText().toString(),phone.getText().toString(),
                        address.getText().toString(),District_Spinner.getSelectedItemPosition(),District_Spinner.getSelectedItem().toString()};
                //---------------------回報Listener------------------------------------------------------------
                Response.Listener<JSONObject> RL=new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response.has("access_token")){
                                try{
                                    preference.edit().putString("access_token",response.getString("access_token")).commit();
                                }catch (JSONException e){

                                }
                                Intent intent = new Intent();
                                intent.setClass(RegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    };
                //---------------------錯誤回報Listener------------------------------------------------------------
                    Response.ErrorListener REL=new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(RegistrationActivity.this)
                                    .setTitle("Error")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setMessage(error.toString())
                                    .show();
                        }
                    };
                //----------------------執行請求----------------------------------------------------------------
//                new myJsonRequest(url,"post",key,value,context,RL,REL).Fire();
                    myJsonRequest.POST_Request.getJSON_object(url,key,value,context,RL,REL);
            }

        }
        //---------------------驗證副程式------------------------------------------------------------
        private boolean verification(){
            boolean Flag=true;
            if(TextUtils.isEmpty(account.getText().toString())){
                account_error.setText("此欄位必填");
                Flag=false;
            }else account_error.setText("");
            if(TextUtils.isEmpty(PW.getText().toString())){
                PW_error.setText("此欄位必填");
                Flag=false;
            }else{
                PW_error.setText("");
                if(PWcomfirm.getText().toString().equals(PW.getText().toString())){
                    PWcomfirm_error.setText("");
                }else {
                    PWcomfirm_error.setText("密碼不相符");
                    Flag=false;
                }
            }
            if(TextUtils.isEmpty(name.getText().toString())){
                name_error.setText("此欄位必填");
            }else name_error.setText("");
            if(TextUtils.isEmpty(idCode.getText().toString())){
                idCode_error.setText("此欄位必填");
                Flag=false;
            }else idCode_error.setText("");
            if(TextUtils.isEmpty(address.getText().toString())){
                address_error.setText("此欄位必填");
                Flag=false;
            }else address_error.setText("");
            if(genderGroup.getCheckedRadioButtonId()==-1){
                gender_error.setText("請選擇性別");
                Flag=false;
            }else gender_error.setText("");
            if(District_Spinner.getSelectedItemPosition()==0){
                district_error.setText("請選擇地區");
                Flag=false;
            }else district_error.setText("");
            if(how2Pay_Spinner.getSelectedItemPosition()==0){
                how2Pay_error.setText("請選擇付款方式");
                Flag=false;
            }else how2Pay_error.setText("");
            return Flag;
        }
    };
//-----------------------------返回按鈕Listener------------------------------------------------------------------------------------------------------------------------
    private Button.OnClickListener go_backListener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
