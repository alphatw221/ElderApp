package com.elderApp.ElderApp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elderApp.ElderApp.Activity.TabActivity;
import com.elderApp.ElderApp.AlertDialog.ios_style_alert_dialog_1;
import com.elderApp.ElderApp.Helper_Class.MySingleton;
import com.elderApp.ElderApp.Model_Class.User;
import com.elderApp.ElderApp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class give_money_comfirm_Frag extends Fragment {
    private Button give_money_comfirm_comfirm,give_money_comfirm_cancel;
    private EditText give_money_comfirm_amount,give_money_comfirm_message;
    private TextView give_money_object;
    private Context context;
    private User user;
    private String take_id,take_name,take_email,amount,message;

    public give_money_comfirm_Frag(String id,String name,String email) {
        this.take_id=id;
        this.take_name=name;
        this.take_email=email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_give_money_comfirm, container, false);
        context=getContext();
        this.user= TabActivity.user;
        give_money_comfirm_comfirm=(Button)view.findViewById(R.id._give_money_comfirm_comfirm);
        give_money_comfirm_cancel=(Button)view.findViewById(R.id._give_money_comfirm_cancel);
        give_money_object=(TextView)view.findViewById(R.id._give_money_comfirm_object);
        give_money_comfirm_message=(EditText)view.findViewById(R.id._give_money_comfirm_message);
        give_money_comfirm_amount=(EditText)view.findViewById(R.id._give_money_comfirm_amount);

        give_money_comfirm_comfirm.setOnClickListener(comfirm_listener);
        give_money_comfirm_cancel.setOnClickListener(cancel_listener);
        give_money_object.setText(take_name);
        return view;
    }


    //---------------------支付按鈕Listener------------------------------------------------------------
    private Button.OnClickListener comfirm_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            amount=give_money_comfirm_amount.getText().toString();
            message=give_money_comfirm_message.getText().toString();
            String url="https://app.happybi.com.tw/api/transaction";
//            Object[] key=new Object[]{"give_id","give_email","take_id","take_email","amount"};
//            Object[] value=new Object[]{
//                    user.user_id,
//                    user.email,
//                    Integer.parseInt(take_id),
//                    take_email,
//                    Integer.parseInt(amount)
//            };
//            myJsonRequest.POST_Request.get_string(url,key,value,context,RL,REL);


            StringRequest sj=new StringRequest(1,url,RL,REL){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization","Bearer "+getActivity().getSharedPreferences("preFile",MODE_PRIVATE).getString("access_token",""));
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject body = new JSONObject();
                    try {
                        body.put("give_id", user.user_id);
                        body.put("give_email", user.email);
                        body.put("take_id", Integer.parseInt(take_id));
                        body.put("take_email", take_email);
                        body.put("amount", Integer.parseInt(amount));
                        body.put("event",message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return body.toString().getBytes();
                }
            };
            MySingleton.getInstance(context).getRequestQueue().add(sj);

        }
    };
    //---------------------取消支付按紐Listener------------------------------------------------------------
    private Button.OnClickListener cancel_listener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            Fragment fragment=FM.findFragmentByTag("give_money_Frag");
            Fragment fragment2=FM.findFragmentByTag("give_money_comfirm_Frag");
            if ( fragment!=null) {
                if ( fragment.isAdded()) {
                    FT.show(fragment);
                    FT.remove(fragment2);
                } else {
//                FT.add(R.id._frag1_fragment,FM.findFragmentByTag("take_money_Frag"),"take_money_Frag").commit();
                    FT.add(R.id._fragment_frag1_blank, fragment, "give_money_comfirm_Frag");
                    FT.remove(fragment2);
                }
            } else{
                FT.replace(R.id._fragment_frag1_blank,new give_money_Frag(),"give_money_Frag");

            }
            FT.commit();
        }
    };

    //---------------------回報Listener------------------------------------------------------------
    private  Response.Listener RL=new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            new ios_style_alert_dialog_1.Builder(context)
                    .setTitle("支付結果")
                    .setMessage(response.toString())
                    .show();




            Fragment fragment=getFragmentManager().findFragmentByTag("give_money_comfirm_Frag");
            getFragmentManager().beginTransaction().replace(R.id._fragment_frag1_blank,new give_money_Frag(),"give_money_Frag").commit();
        }


    };
    //---------------------錯誤回報Listener------------------------------------------------------------
    private Response.ErrorListener REL=new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            new ios_style_alert_dialog_1.Builder(context)
                    .setTitle("錯誤")
                    .setMessage("支付失敗"+error.toString())
                    .show();

            Fragment fragment=getFragmentManager().findFragmentByTag("give_money_comfirm_Frag");
            getFragmentManager().beginTransaction().replace(R.id._fragment_frag1_blank,new give_money_Frag(),"give_money_Frag").commit();
            //
        }
    };
}
