package com.htlc.cjwl.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.OrdersPagerAdaptor;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.LogUtil;


/**
 * Created by Administrator on 2015/10/28.
 */
public class OrdersFragment extends Fragment /*implements ActionBar.TabListener*/ {
    private LinearLayout mTabsLinearLayout;
    private OrdersPagerAdaptor adapter;//三中订单fragment的adapter
    private PagerSlidingTabStrip indicator;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_orders_fragment, null);
        TextView tv_fragment_title = (TextView) view.findViewById(R.id.tv_fragment_title);
        tv_fragment_title.setText("我的订单");
        indicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        init();
        return view;
    }

    public void init() {
        initTab();
        adapter = new OrdersPagerAdaptor(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                indicator.setTranslationX(0);
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
                    if (i == position) {
                        adapter.getItem(position).refreshData();
                        tv.setTextColor(getResources().getColor(R.color.blue));
                    } else {
                        tv.setEnabled(true);
                        tv.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mTabsLinearLayout = (LinearLayout) indicator.getChildAt(0);
        for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
            if (i == 0) {
                tv.setTextColor(getResources().getColor(R.color.blue));
            } else {
                tv.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    public void initTab() {
        //tab 宽度均分
        indicator.setShouldExpand(true);

        /**
         * pstsIndicatorColor   滑动条的颜色
         pstsUnderlineColor  滑动条所在的那个全宽线的颜色
         pstsDividerColor  每个标签的分割线的颜色
         pstsIndicatorHeight       滑动条的高度
         pstsUnderlineHeight 滑动条所在的那个全宽线的高度
         pstsDividerPadding 分割线底部和顶部的填充宽度
         pstsTabPaddingLeftRight   每个标签左右填充宽度
         pstsScrollOffset       Scroll offset of the selected tab
         pstsTabBackground   每个标签的背景，应该是一个StateListDrawable
         pstsShouldExpand   如果设置为true，每个标签是相同的控件，均匀平分整个屏幕，默认是false
         pstsTextAllCaps    如果为true，所有标签都是大写字母，默认为true
         */
        indicator.setTextSize(CommonUtil.dp2px(this.getActivity(), 14));
        //设置下划线
        indicator.setUnderlineColor(this.getResources().getColor(R.color.light_gray));
        indicator.setUnderlineHeight(CommonUtil.dp2px(this.getActivity(), 1));
        //设置滑动指示线
        indicator.setIndicatorColor(this.getResources().getColor(R.color.blue));
        indicator.setIndicatorHeight(CommonUtil.dp2px(this.getActivity(), 3));
        //设置tab间分割线
        indicator.setDividerColor(Color.TRANSPARENT);
        //设置背景颜色
        indicator.setBackgroundColor(this.getResources().getColor(R.color.white));
    }


}
