package com.htlc.cjwl.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.OrdersPagerAdaptor;
import com.htlc.cjwl.util.CommonUtil;

/**
 * Created by sks on 2016/4/7.
 */
public class InvoiceActivity extends AppCompatActivity {
    private LinearLayout mTabsLinearLayout;
    private OrdersPagerAdaptor adapter;//三中订单fragment的adapter
    private PagerSlidingTabStrip indicator;
    private ViewPager viewPager;

    private TextView textTitle,textRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("我的退款");
        textRightButton = (TextView) findViewById(R.id.confirm);
        textRightButton.setText("申请退款");
        textRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRefund();
            }
        });
        indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        init();
    }



    public void init() {
        initTab();
        adapter = new OrdersPagerAdaptor(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
                    if (i == position) {
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

    /**
     * 进入退款界面
     */
    private void submitRefund() {

    }
    public void initTab() {
        //tab 宽度均分
        indicator.setShouldExpand(true);
        indicator.setTextSize(CommonUtil.dp2px(this, 14));
        //设置下划线
        indicator.setUnderlineColor(this.getResources().getColor(R.color.light_gray));
        indicator.setUnderlineHeight(CommonUtil.dp2px(this, 1));
        //设置滑动指示线
        indicator.setIndicatorColor(this.getResources().getColor(R.color.blue));
        indicator.setIndicatorHeight(CommonUtil.dp2px(this, 3));
        //设置tab间分割线
        indicator.setDividerColor(Color.TRANSPARENT);
        //设置背景颜色
        indicator.setBackgroundColor(this.getResources().getColor(R.color.white));
    }
}
