package com.htlc.cjwl.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.ServiceInfoBean;
import com.htlc.cjwl.util.AnimateFirstDisplayListener;
import com.htlc.cjwl.util.ImageLoaderCfg;
import com.htlc.cjwl.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import model.TraceBean;

/**
 * Created by sks on 2015/10/29.
 */
public class TraceAdapter extends BaseAdapter {
    private ArrayList<TraceBean> items;
    private Context context;
    public TraceAdapter(Context context, ArrayList<TraceBean> items) {
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
            convertView = View.inflate(context, R.layout.adapter_trace,null);
            holder.up_view = convertView.findViewById(R.id.up_view);
            holder.down_view = convertView.findViewById(R.id.down_view);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textAddress = (TextView) convertView.findViewById(R.id.textAddress);
            holder.textTime = (TextView) convertView.findViewById(R.id.textTime);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TraceBean bean = items.get(position);
        holder.textAddress.setText(bean.order_address);
        holder.textTime.setText(bean.order_date);
        if(position == 0){
            holder.imageView.setImageResource(R.drawable.circle_blue_shape);
            holder.up_view.setVisibility(View.INVISIBLE);
            holder.down_view.setVisibility(View.VISIBLE);
        }else if(position == items.size()-1){
            holder.imageView.setImageResource(R.drawable.circle_gray_shape);
            holder.up_view.setVisibility(View.VISIBLE);
            holder.down_view.setVisibility(View.INVISIBLE);
        }else {
            holder.imageView.setImageResource(R.drawable.circle_gray_shape);
            holder.up_view.setVisibility(View.VISIBLE);
            holder.down_view.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class ViewHolder{
        View up_view,down_view;
        TextView textAddress,textTime;
        ImageView imageView;
    }
}
