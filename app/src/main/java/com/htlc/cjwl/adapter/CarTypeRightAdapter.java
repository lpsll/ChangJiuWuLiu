package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/5.
 */
public class CarTypeRightAdapter extends BaseAdapter {
    private ArrayList list;
    private Activity activity;

    public CarTypeRightAdapter(Activity activity, ArrayList list) {
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
            convertView = View.inflate(activity, R.layout.adapter_car_type_right,null);
            holder.textCarTypeName = (TextView) convertView.findViewById(R.id.textCarTypeName);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textCarTypeName.setText("X"+position);
        return convertView;
    }
    class ViewHolder{
        TextView textCarTypeName;
    }
}
