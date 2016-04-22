package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.WebActivity;
import com.htlc.cjwl.bean.RouteQueryFootInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/5.
 */
public class RouteQueryAdapter extends PagerAdapter {
    private ArrayList<RouteQueryFootInfoBean> list;
    private Activity activity;
    public RouteQueryAdapter(Activity activity,ArrayList<RouteQueryFootInfoBean> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(CommonUtil.getApplication(), R.layout.layout_route_foot_image_view,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_foot_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(RouteQueryAdapter.this, "点击了图片：" + position);
                Intent intent = new Intent(activity, WebActivity.class);
                String url = String.format("",list.get(position).id);
                intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
                intent.putExtra(Constant.SERVICE_DETAIL_TITLE, "详情");
                activity.startActivity(intent);
            }
        });
        ImageLoader.getInstance().displayImage(list.get(position).image,imageView);
        container.addView(view);
        return view;
    }
}
