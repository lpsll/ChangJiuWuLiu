package com.htlc.cjwl.adapter;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.htlc.cjwl.R;
import com.htlc.cjwl.fragment.OrderStateFragment;
import com.htlc.cjwl.util.CommonUtil;

/**
 * Created by Larno 2016/04/01
 */
public class OrdersPagerAdaptor extends FragmentStatePagerAdapter {
    private String[] titles;
    public OrdersPagerAdaptor(FragmentManager fm) {
        super(fm);
        titles = CommonUtil.getStringArray(R.array.order_state_title);
    }
    private OrderStateFragment[] fragments = new OrderStateFragment[5];
    @Override
    public OrderStateFragment getItem(int position) {
        if (fragments[position] != null) {
            return fragments[position];
        }
        fragments[position] = OrderStateFragment.newInstance(position);
        return fragments[position];
    }
    @Override
    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
