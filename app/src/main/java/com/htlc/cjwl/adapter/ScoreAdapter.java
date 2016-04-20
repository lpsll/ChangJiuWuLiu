package com.htlc.cjwl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

import model.RefundOrderBean;

/**
 * Created by Larno on 16/04/07.
 */
public class ScoreAdapter extends BaseAdapter{

    private ArrayList<RefundOrderBean> ordersList;
    private Context context;

    public ScoreAdapter(ArrayList<RefundOrderBean> ordersList, Context context) {
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
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_refund_list, null);
            holder = new ViewHolder();
            holder.textTime = (TextView) convertView.findViewById(R.id.textTime);
            holder.textOrderId = (TextView) convertView.findViewById(R.id.textOrderId);
            holder.textPrice = (TextView) convertView.findViewById(R.id.textPrice);
            holder.textComment = (TextView) convertView.findViewById(R.id.textComment);
            holder.textState = (TextView) convertView.findViewById(R.id.textState);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        RefundOrderBean bean = ordersList.get(position);
        holder.textTime.setText(bean.order_date);
        holder.textOrderId.setText("订单号: "+bean.order_no);
        holder.textPrice.setText("金额: "+bean.order_price);
        //"order_ispay": "2"//2已付款(历史订单) ｛3待退款 4已退款｝（历史退款）
        if("3".equals(bean.order_ispay)){
            holder.textState.setText("申请中");
        }else {
            holder.textState.setText("已退款");
        }
        holder.textComment.setText("备注: "+bean.order_remark);
        return convertView;
    }
    class ViewHolder {
        TextView textOrderId;//订单编号
        TextView textPrice;
        TextView textComment;
        TextView textState;
        TextView textTime;//订单日期
    }
}

