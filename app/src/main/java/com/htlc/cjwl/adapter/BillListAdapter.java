package com.htlc.cjwl.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

import model.BillDetailBean;
import model.RefundOrderBean;

/**
 * Created by Larno on 16/04/07.
 */
public class BillListAdapter extends BaseAdapter{

    private int id;
    private ArrayList<BillDetailBean> ordersList;
    private Context context;
    private final String html = "<font color=\"#3c3c3c\">时间:  </font><font color=\"#0a198c\">%1$s</font>" ;

    public BillListAdapter(int id, ArrayList ordersList, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_bill_list, null);
            holder = new ViewHolder();
            holder.textHeader = (TextView) convertView.findViewById(R.id.textTime);
            holder.textPrice = (TextView) convertView.findViewById(R.id.textOrderId);
            holder.textType = (TextView) convertView.findViewById(R.id.textPrice);
            holder.textTime = (TextView) convertView.findViewById(R.id.textComment);
            holder.textState = (TextView) convertView.findViewById(R.id.textState);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        BillDetailBean bean = ordersList.get(position);
        holder.textHeader.setText("发票抬头: "+bean.invoice_buyer);
        holder.textType.setText("发票类型: "+bean.invoice_type);
        holder.textPrice.setText("价格: "+bean.invoice_price);
        ////1申请中，2已打印
        if("1".equals(bean.invoice_flag)){
            holder.textState.setText("申请中");
        }else {
            holder.textState.setText("已打印");
        }
        String comment = String.format(html, bean.invoice_date);
        holder.textTime.setText(Html.fromHtml(comment));
        return convertView;
    }
    class ViewHolder {
        TextView textHeader;
        TextView textPrice;
        TextView textType;
        TextView textState;
        TextView textTime;//订单日期
    }
}

