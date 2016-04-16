package com.htlc.cjwl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.widget.RefundListSelectItem;

import java.util.ArrayList;

import model.RefundOrderBean;

/**
 * Created by Larno on 16/04/07.
 */
public class RefundListSelectAdapter extends BaseAdapter{

    private int id;
    private ArrayList<RefundOrderBean> ordersList;
    private Context context;

    public RefundListSelectAdapter(int id, ArrayList<RefundOrderBean> ordersList, Context context) {
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
        if(convertView == null){
            convertView = new RefundListSelectItem(context);
        }
        RefundOrderBean bean = ordersList.get(position);
        RefundListSelectItem refundListSelectItem = (RefundListSelectItem) convertView;
        refundListSelectItem.textTime.setText(bean.order_date);
        refundListSelectItem.textOrderId.setText("订单号: "+bean.order_no);
        refundListSelectItem.textPrice.setText("金额: "+bean.order_price);
        return convertView;
    }

}

