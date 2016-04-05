package com.htlc.cjwl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.HotCity;

import java.util.List;

/**
 * Created by luochuan on 15/11/11.
 */
public class HotcityAdapter extends BaseAdapter {

    private Context context;
    private List<HotCity> list;
    private TextView hotCity;
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }
    public HotcityAdapter(Context context, List<HotCity> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = (LinearLayout) convertView.inflate(context, R.layout.layout_hot_city_item,null);
        TextView tv = (TextView) view.getChildAt(0);
        tv.setText(list.get(position).getCityname());
        tv.setBackgroundResource(R.drawable.hot_city_btn0);
        if (clickTemp == position) {
            tv.setBackgroundResource(R.drawable.hot_city_btn1);
            tv.setTextColor(Color.WHITE);
        } else {
            tv.setBackgroundResource(R.drawable.hot_city_btn0);
        }
        return view;
    }
}
