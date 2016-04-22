package com.htlc.cjwl.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.ServiceInfoBean;
import com.htlc.cjwl.util.AnimateFirstDisplayListener;
import com.htlc.cjwl.util.ImageLoaderCfg;
import util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by sks on 2015/10/29.
 */
public class ServiceListViewAdapter extends BaseAdapter {
    private ArrayList<ServiceInfoBean> items;
    private Context context;
    public ServiceListViewAdapter(Context context,ArrayList<ServiceInfoBean> items) {
        this.items = items;
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_service_list,null);
            holder.iv_service_adapter = (ImageView) convertView.findViewById(R.id.iv_service_adapter);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ServiceInfoBean bean = items.get(position);
        LogUtil.i(this, "bean.service_image=" + bean.service_image);
        ImageLoader.getInstance().displayImage(
                bean.service_image, holder.iv_service_adapter,
                ImageLoaderCfg.options, animateFirstListener);
        return convertView;
    }
    protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    class ViewHolder{
        ImageView iv_service_adapter;
    }
}
