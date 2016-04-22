package com.htlc.cjwl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.CallServiceTelActivity;
import com.htlc.cjwl.activity.NetworkQueryActivity;
import com.htlc.cjwl.activity.OrderInfoActivity;
import com.htlc.cjwl.activity.WebActivity;
import com.htlc.cjwl.adapter.HomeAdapter;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.util.Constant;
import util.LogUtil;
import com.htlc.cjwl.util.LoginUtil;
import util.ToastUtil;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;


/**
 * Created by sks on 2015/10/28.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeAdapter adapter;
    private ArrayList<HomeBannerInfo> list = new ArrayList<HomeBannerInfo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home_fragment, null);

        //fragment的标题
        TextView tv_fragment_title = (TextView) view.findViewById(R.id.tv_fragment_title);
        tv_fragment_title.setText(R.string.home_fragment_title);

        //顶部  图片banner 控件
        ViewPager vp_home = (ViewPager) view.findViewById(R.id.vp_home);
        //圆形的  我要运车 控件
        TextView tv_consign = (TextView) view.findViewById(R.id.tv_consign);
        //网点查询
        ImageButton ib_home_network_query = (ImageButton) view.findViewById(R.id.ib_home_network_query);
        //路线查询
        ImageButton ib_home_rute_query = (ImageButton) view.findViewById(R.id.ib_home_rute_query);
        //一键客服
        ImageButton ib_home_phone = (ImageButton) view.findViewById(R.id.ib_home_phone);

        tv_consign.setOnClickListener(this);
        ib_home_network_query.setOnClickListener(this);
        ib_home_rute_query.setOnClickListener(this);
        ib_home_phone.setOnClickListener(this);

//        list.add(new HomeBannerInfo());
        adapter = new HomeAdapter(getActivity(), list);
        vp_home.setAdapter(adapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 获取首页banner图片数据
     */
    private void initData() {
        App.appAction.homeBanner(new ActionCallbackListener<ArrayList<HomeBannerInfo>>() {
            @Override
            public void onSuccess(ArrayList<HomeBannerInfo> data) {
                list.clear();
                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });

    }

    /**
     * 我要运车，网点查询，路线查询，一键客服；点击事件的处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_consign:
                if (!LoginUtil.isOnline()) {
                    LoginUtil.goLogin(getActivity());
                } else {
                    Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_home_network_query:
                LogUtil.i(this, "ib_home_network_query");
                Intent intent_network_query = new Intent(getActivity(), NetworkQueryActivity.class);
                startActivity(intent_network_query);
                break;
            case R.id.ib_home_rute_query:
                LogUtil.i(this, "ib_home_rute_query");
//                Intent intent_rute_query = new Intent(getActivity(), RuteQueryActivity.class);
                Intent intent_rute_query = new Intent(getActivity(), WebActivity.class);
                String serviceID = String.format(Api.ServiceHtmlDetail,"20");
                intent_rute_query.putExtra(Constant.SERVICE_DETAIL_ID, serviceID);
                intent_rute_query.putExtra(Constant.SERVICE_DETAIL_TITLE, "托运须知");
                startActivity(intent_rute_query);
                break;
            case R.id.ib_home_phone:
                LogUtil.i(this, "ib_home_phone");
                Intent intent_phone = new Intent(getActivity(), CallServiceTelActivity.class);
                startActivity(intent_phone);
                break;
        }
    }
}
