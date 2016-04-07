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
 * Created by Larno on 16/04/07.
 */
public class RefundListAdapter extends BaseAdapter{

    private int id;
    private ArrayList<OrderInfoBean> ordersList;
    private Context context;

    public RefundListAdapter(int id, ArrayList<OrderInfoBean> ordersList, Context context) {
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
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_refund_list, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        return convertView;
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

