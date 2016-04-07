package com.htlc.cjwl.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.htlc.cjwl.R;
import com.htlc.cjwl.fragment.OrderStateFragment;
import com.htlc.cjwl.fragment.RefundStateFragment;
import com.htlc.cjwl.util.CommonUtil;

/**
 * Created by Larno 2016/04/01
 */
public class RefundPagerAdaptor extends FragmentStatePagerAdapter {
    private String[] titles;
    public RefundPagerAdaptor(FragmentManager fm) {
        super(fm);
        titles = CommonUtil.getStringArray(R.array.refund_state_title);
    }
    private Fragment[] fragments = new Fragment[2];

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] != null) {
            return fragments[position];
        }
        fragments[position] = RefundStateFragment.newInstance(position);
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
