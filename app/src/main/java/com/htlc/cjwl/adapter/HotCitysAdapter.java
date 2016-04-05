package com.htlc.cjwl.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/4.
 */
public class HotCitysAdapter extends BaseAdapter{

    private ArrayList<CityInfoBean> list;

    public HotCitysAdapter(ArrayList<CityInfoBean> list) {
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
        ViewHolder holder =null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(CommonUtil.getApplication(), R.layout.layout_hot_city_item,null);
            holder.tv_hot_city = (TextView) convertView.findViewById(R.id.tv_hot_city);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_hot_city.setText(list.get(position).cityname);
        return convertView;
    }
    class ViewHolder{
        TextView tv_hot_city;
    }
}
