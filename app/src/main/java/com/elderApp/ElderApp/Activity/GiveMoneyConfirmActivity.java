package com.elderApp.ElderApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.elderApp.ElderApp.Helper_Class.AlertHandler;
import com.elderApp.ElderApp.Helper_Class.ErrorHandler;
import com.elderApp.ElderApp.Helper_Class.apiService;
import com.elderApp.ElderApp.R;

public class GiveMoneyConfirmActivity extends AppCompatActivity {

    private Context context;

    private ImageButton exitImageButton;
    private TextView nameTextView;
    private EditText messageEditText,amountEditText;
    private Button submitButton,cancelButton;

    private String take_id;
    private String take_name;
    private String take_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_give_money_confirm);
        take_id = getIntent().getStringExtra("take_id");
        take_name = getIntent().getStringExtra("take_name");
        take_email = getIntent().getStringExtra("take_email");

        exitImageButton = findViewById(R.id.exitImageButton);
        nameTextView = findViewById(R.id.nameTextView);
        amountEditText = findViewById(R.id.amountEditText);
        messageEditText = findViewById(R.id.messageEditText);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);
        nameTextView.setText(take_name);


        exitImageButton.setOnClickListener(exitListener);
        cancelButton.setOnClickListener(exitListener);
        submitButton.setOnClickListener(submitListener);

    }


    private Button.OnClickListener exitListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private Button.OnClickListener submitListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            String amount = amountEditText.getText().toString();
            String message = messageEditText.getText().toString();
            if(amount.isEmpty()){
                ErrorHandler.alert(context,"訊息","請輸入金額");
                return;
            }

            apiService.transactionRequest(context, take_id, take_email, amount, message, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    AlertHandler.alert(context, "訊息", "支付成功", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.errorMessage(error);
                }
            });
        }
    };






}