package com.htlc.cjwl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.cjwl.activity.OrderInfoActivity;
import com.htlc.cjwl.adapter.MainPagerAdapter;
import com.htlc.cjwl.fragment.HomeFragment;
import com.htlc.cjwl.fragment.MyFragment;
import com.htlc.cjwl.fragment.OrdersFragment;
import com.htlc.cjwl.fragment.ServiceFragment;

import java.util.ArrayList;

import util.LogUtil;
/**
 * 主界面
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SelectPosition = "SelectPosition";
    public static final int RequestCode = 1000;
    //首页tab
    private ImageView iv_tab1;
    private TextView tv_tab1;
    //订单列表tab
    private ImageView iv_tab2;
    private TextView tv_tab2;
    //产品推介tab
    private ImageView iv_tab3;
    private TextView tv_tab3;
    //个人中心tab
    private ImageView iv_tab4;
    private TextView tv_tab4;

    //当前选中的fragment的tab
    private ImageView m_iv_tab;
    private TextView m_tv_tab;

    //屏幕宽度
    private int mWidth;
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private ArrayList<Fragment> list;

    /*去生成预生成订单信息界面（我要运车）*/
    public void goOrderInfo(){
        Intent intent = new Intent(this, OrderInfoActivity.class);
        startActivityForResult(intent, MainActivity.RequestCode);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /*（我要运车）界面返回结果的处理(下单成功，则跳转到我的订单Tab)*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            String selectPosition = data.getStringExtra(SelectPosition);
            if (requestCode == RequestCode && !TextUtils.isEmpty(selectPosition) && selectPosition.equals(OrdersFragment.class.getSimpleName())) {
                if (viewPager != null && tv_tab2 != null && iv_tab2 != null) {
                    resetPreTab();
                    setTab(tv_tab2, iv_tab2);
                }
            }
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取屏幕宽度
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        fragmentManager = getSupportFragmentManager();
        initView();
    }


    /**
     * 初始化组件
     */
    private void initView() {
        //给fragment的每个标签设置点击事件，并设置每个标签的宽度为屏幕宽度的1/4;
        iv_tab1 = (ImageView) findViewById(R.id.iv_tab1);
        iv_tab1.measure(0, 0);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_tab1.getLayoutParams();
        layoutParams1.width = mWidth / 4;
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        iv_tab1.setOnClickListener(this);
        tv_tab1.setOnClickListener(this);

        iv_tab2 = (ImageView) findViewById(R.id.iv_tab2);
        iv_tab2.measure(0, 0);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_tab2.getLayoutParams();
        layoutParams2.width = mWidth / 4;
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        iv_tab2.setOnClickListener(this);
        tv_tab2.setOnClickListener(this);

        iv_tab3 = (ImageView) findViewById(R.id.iv_tab3);
        iv_tab3.measure(0, 0);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) iv_tab3.getLayoutParams();
        layoutParams3.width = mWidth / 4;
        tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
        iv_tab3.setOnClickListener(this);
        tv_tab3.setOnClickListener(this);

        iv_tab4 = (ImageView) findViewById(R.id.iv_tab4);
        iv_tab4.measure(0, 0);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) iv_tab4.getLayoutParams();
        layoutParams4.width = mWidth / 4;
        tv_tab4 = (TextView) findViewById(R.id.tv_tab4);
        iv_tab4.setOnClickListener(this);
        tv_tab4.setOnClickListener(this);

        //初始化4个Fragment
        list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new OrdersFragment());
        list.add(new ServiceFragment());
        list.add(new MyFragment());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainPagerAdapter(fragmentManager, list));
        viewPager.setOffscreenPageLimit(4);

        //默认选中第一个标签，即选中home fragment
        setTab(tv_tab1, iv_tab1);
    }

    /**
     * fragment标签的点击事件处理，切换到对应的fragment
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_tab1:
            case R.id.tv_tab1:
                LogUtil.i(this, "iv_tab1");
                resetPreTab();
                setTab(tv_tab1, iv_tab1);
                break;
            case R.id.iv_tab2:
            case R.id.tv_tab2:
                LogUtil.i(this, "iv_tab2");
                resetPreTab();
                setTab(tv_tab2, iv_tab2);
                break;
            case R.id.iv_tab3:
            case R.id.tv_tab3:
                LogUtil.i(this, "iv_tab3");
                resetPreTab();
                setTab(tv_tab3, iv_tab3);

                break;
            case R.id.iv_tab4:
            case R.id.tv_tab4:
                LogUtil.i(this, "iv_tab4");
                resetPreTab();
                setTab(tv_tab4, iv_tab4);
                break;
        }


    }

    /**
     * 设置选中标签的状态，并切换到对应的fragment
     *
     * @param tv_tab
     * @param iv_tab
     */
    private void setTab(TextView tv_tab, ImageView iv_tab) {
        iv_tab.setEnabled(false);
        tv_tab.setEnabled(false);
        int id = iv_tab.getId();
        switch (id) {
            case R.id.iv_tab1:
                iv_tab1.setImageResource(R.drawable.home_fragment_tab_press);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.iv_tab2:
                iv_tab2.setImageResource(R.drawable.orders_fragment_tab_press);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.iv_tab3:
                iv_tab3.setImageResource(R.drawable.service_fragment_tab_press);
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.iv_tab4:
                iv_tab4.setImageResource(R.drawable.my_fragment_tab_press);
                viewPager.setCurrentItem(3, false);
                ((MyFragment) list.get(3)).updateView();
                break;
        }
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        sa.setDuration(500);
        sa.setFillAfter(true);
        iv_tab.startAnimation(sa);
        m_iv_tab = iv_tab;
        m_tv_tab = tv_tab;
    }

    /**
     * 重置已选中的标签
     */
    private void resetPreTab() {
        if (m_iv_tab != null) {
            m_iv_tab.setEnabled(true);
            m_tv_tab.setEnabled(true);
            int id = m_iv_tab.getId();
            switch (id) {
                case R.id.iv_tab1:
                    m_iv_tab.setImageResource(R.drawable.home_fragment_tab_normal);
                    break;
                case R.id.iv_tab2:
                    m_iv_tab.setImageResource(R.drawable.orders_fragment_tab_normal);
                    break;
                case R.id.iv_tab3:
                    m_iv_tab.setImageResource(R.drawable.service_fragment_tab_normal);
                    break;
                case R.id.iv_tab4:
                    m_iv_tab.setImageResource(R.drawable.my_fragment_tab_normal);
                    break;
            }
            ScaleAnimation sa = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
            sa.setDuration(500);
            sa.setFillAfter(true);
            m_iv_tab.startAnimation(sa);
        }
    }
}
