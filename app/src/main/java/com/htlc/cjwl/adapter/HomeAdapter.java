package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.WebActivity;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/5.
 */
public class HomeAdapter extends PagerAdapter {
    private ArrayList<HomeBannerInfo> list;
    private Activity activity;
    public HomeAdapter(Activity activity, ArrayList<HomeBannerInfo> list) {
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
        View view = View.inflate(CommonUtil.getApplication(), R.layout.adapter_home_pager,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_home_adapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(HomeAdapter.this, "点击了图片：" + position);
                Intent intent = new Intent(activity, WebActivity.class);
                String weburl = list.get(position).weburl;
                String url = null;
                if(!TextUtils.isEmpty(weburl)){
                    url = String.format(weburl);
                    intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
                    intent.putExtra(Constant.SERVICE_DETAIL_TITLE, "详情");
                    activity.startActivity(intent);
                }else{
                    url = String.format("http://www.baidu.com");
                }

            }
        });

        String image = list.get(position).image;
        LogUtil.i(this, "TextUtils.isEmpty(image)？" + TextUtils.isEmpty(image));
//        if(!TextUtils.isEmpty(image)){
//            LogUtil.i(this,"display");
//            ImageLoader.getInstance().displayImage(image, imageView);
//        }else {
            LogUtil.i(this, "default");
            imageView.setImageResource(R.drawable.home_car);
//        }
        container.addView(view);
        return view;
    }
}
