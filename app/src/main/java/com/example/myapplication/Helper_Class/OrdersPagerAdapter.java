package com.example.myapplication.Helper_Class;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.Activity.TabActivity;
import com.example.myapplication.Fragment.Frag1;
import com.example.myapplication.Fragment.Frag2;
import com.example.myapplication.Fragment.Frag3;
import com.example.myapplication.Fragment.event_detial_Frag;
import com.example.myapplication.Fragment.frag1_blank;
import com.example.myapplication.Fragment.frag2_blank;
import com.example.myapplication.Fragment.frag3_blank;
import com.example.myapplication.Fragment.give_money_Frag;
import com.example.myapplication.Fragment.market_Frag;
import com.example.myapplication.Fragment.my_transaction_Frag;
import com.example.myapplication.Fragment.take_money_Frag;
import com.example.myapplication.Model_Class.Event_class;
import com.example.myapplication.Model_Class.User;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.List;

public class OrdersPagerAdapter extends FragmentStateAdapter {
    private Fragment Frag1,Frag2,Frag3;
    private List<Event_class> e;

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            default:
                return new frag1_blank();
            case 1:
                return new frag2_blank(e);
            case 2:
                return new frag3_blank();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void set_frag2(List<Event_class> e){
        this.e=e;
    }
}
