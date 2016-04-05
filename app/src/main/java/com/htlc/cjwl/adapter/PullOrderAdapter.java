package com.htlc.cjwl.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by luochuan on 15/11/17.
 */
public class PullOrderAdapter extends BaseAdapter{

    private int id;
    private ArrayList<OrderInfoBean> ordersList;
    private Context context;

    public PullOrderAdapter(int id, ArrayList<OrderInfoBean> ordersList,Context context) {
        this.id = id;
        this.ordersList = ordersList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LogUtil.i(this, "list view条目数量：" + ordersList.size() + ";id=" + id);
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_order_item, null);
            holder = new ViewHolder();
            holder.tvOrderNo = (TextView) convertView.findViewById(R.id.order_no);
            holder.tvorderDep = (TextView) convertView.findViewById(R.id.order_departure);
            holder.tvOrderDes = (TextView) convertView.findViewById(R.id.order_destination);
            holder.tvOrderDate = (TextView) convertView.findViewById(R.id.order_date);
            holder.tvOrderState = (TextView) convertView.findViewById(R.id.order_state);
            holder.tvOrderCancel = (TextView) convertView.findViewById(R.id.order_cancel);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        OrderInfoBean orderInfoBean = ordersList.get(position);
        holder.tvOrderNo.setText(orderInfoBean.order_no);
        if(TextUtils.isEmpty(orderInfoBean.to_address.trim())){
            holder.tvOrderDes.setText(orderInfoBean.to_cityname);
        }else{
            holder.tvOrderDes.setText(orderInfoBean.to_address);
        }
        if(TextUtils.isEmpty(orderInfoBean.from_address.trim())){
            holder.tvorderDep.setText(orderInfoBean.from_cityname);
        }else{
            holder.tvorderDep.setText(orderInfoBean.from_address);
        }
        holder.tvOrderDate.setText(orderInfoBean.order_date);
        switch (orderInfoBean.order_status){
            case "1":
                holder.tvOrderState.setText("未确认");
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(position);
                    }
                });
                break;
            case "2":
                holder.tvOrderState.setText("进行中");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                break;
            case "3":
                holder.tvOrderState.setText("已取消");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                break;
            case "4":
                holder.tvOrderState.setText("已完成");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                break;
        }

        return convertView;
    }

    private void cancelOrder(final int position) {
    }


    class ViewHolder {
        TextView tvOrderNo;//订单编号
        TextView tvOrderDes;//目的地
        TextView tvorderDep;//出发地
        TextView tvOrderDate;//订单日期
        TextView tvOrderState;//订单状态
        TextView tvOrderCancel;//订单状态
    }
}

