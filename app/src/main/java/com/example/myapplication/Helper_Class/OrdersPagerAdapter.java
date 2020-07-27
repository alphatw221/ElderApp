package com.example.myapplication.Helper_Class;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragment.Frag1;
import com.example.myapplication.Fragment.Frag2;
import com.example.myapplication.Fragment.Frag3;
import com.example.myapplication.Fragment.event_detial_Frag;
import com.example.myapplication.Model_Class.Event_class;

public class OrdersPagerAdapter extends FragmentStateAdapter {
    private Fragment Frag1,Frag2,Frag3;
    private Frag2.change_frag2 c;
    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity,Frag2.change_frag2 c) {
        super(fragmentActivity);
        this.c=c;
        Frag1=new Frag1();
        Frag2=new Frag2(c);
        Frag3=new Frag3();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            default:
                return Frag1;
            case 1:
                return Frag2;
            case 2:
                return Frag3;

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void change_frag2(Event_class e){
        Frag2=new event_detial_Frag(e);
    }
}
