package com.elderApp.ElderApp.Helper_Class;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elderApp.ElderApp.Fragment.frag1_blank;
import com.elderApp.ElderApp.Fragment.frag2_blank;
import com.elderApp.ElderApp.Fragment.frag3_blank;
import com.elderApp.ElderApp.Fragment.frag4_blank;
import com.elderApp.ElderApp.Model_Class.Event_class;

import java.util.List;

public class OrdersPagerAdapter extends FragmentStateAdapter {
    private Fragment Frag1,Frag2,Frag3,Frag4;
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
                return new frag2_blank();
            case 2:
                return new frag4_blank();
            case 3:
                return new frag3_blank();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}
