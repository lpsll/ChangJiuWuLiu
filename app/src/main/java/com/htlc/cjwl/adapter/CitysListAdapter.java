package com.htlc.cjwl.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/4.
 */
public class CitysListAdapter extends BaseAdapter{

    private ArrayList<CityInfoBean> list;

    public CitysListAdapter(ArrayList<CityInfoBean> list) {
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
            convertView = View.inflate(CommonUtil.getApplication(), R.layout.adapter_city_list,null);
            holder.tv_city_name = (TextView) convertView.findViewById(R.id.tv_city_name);
            holder.tv_group_title = (TextView) convertView.findViewById(R.id.tv_group_title);
            holder.ll_group_title = (LinearLayout) convertView.findViewById(R.id.rl_group_title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CityInfoBean cityInfoBean = list.get(position);

        LogUtil.i(this, cityInfoBean.toString());

        if(position!=0){
            CityInfoBean preBean = list.get(position - 1);
            if(preBean.group.equals(cityInfoBean.group)){
                holder.ll_group_title.setVisibility(View.GONE);
            }else{
                holder.ll_group_title.setVisibility(View.VISIBLE);
                holder.tv_group_title.setText(cityInfoBean.group);
            }
        }else{
            holder.tv_group_title.setText(cityInfoBean.group);
        }
        holder.tv_city_name.setText(cityInfoBean.name);

        return convertView;
    }
    class ViewHolder{
        TextView tv_city_name;
        TextView tv_group_title;
        LinearLayout ll_group_title;
    }
}
