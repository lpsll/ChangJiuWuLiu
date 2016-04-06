package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.OrderInfoActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

import model.CarInfoBean;

/**
 * Created by sks on 2015/11/5.
 */
public class SwipeCarAdapter extends BaseAdapter {
    private ArrayList<CarInfoBean> list;
    private OrderInfoActivity activity;

    public SwipeCarAdapter(OrderInfoActivity activity, ArrayList<CarInfoBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(activity,R.layout.adapter_swipe_car,null);
            holder.textView = (TextView) convertView.findViewById(R.id.textCarName);
            holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageDelete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarInfoBean bean = list.get(position);
        holder.textView.setText(bean.carName);
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                activity.refreshTextCarNum();
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView textView;
        ImageView imageDelete;
    }
}
