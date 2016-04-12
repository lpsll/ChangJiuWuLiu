package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

import model.CarTypeInfoBean;

/**
 * Created by sks on 2015/11/5.
 */
public class CarTypeLeftAdapter extends BaseAdapter {
    private ArrayList list;
    private Activity activity;
    private int checkPostion;

    public CarTypeLeftAdapter(Activity activity, ArrayList list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(activity,R.layout.adapter_car_type_left,null);
            holder.textView = (TextView) convertView.findViewById(R.id.textCarName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == checkPostion){
            holder.imageView.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(Color.WHITE);
        }else {
            holder.imageView.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(0xf3f3f3);
        }
        CarTypeInfoBean bean = (CarTypeInfoBean) list.get(position);
        holder.textView.setText(bean.car_brand);
        return convertView;
    }

    public void setCheckPosition(int position) {
        checkPostion = position;
    }

    public int getCheckPosition() {
        return checkPostion;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
