package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.PayActivity;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.fragment.OrderStateFragment;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;

/**
 * Created by Larno 2016/04/01;
 */
public class PullOrderAdapter extends BaseAdapter{

    private int id;
    private ArrayList<OrderInfoBean> ordersList;
    private Activity context;
    private OrderStateFragment orderStateFragment;


    public PullOrderAdapter(int id, ArrayList<OrderInfoBean> ordersList, FragmentActivity activity, OrderStateFragment orderStateFragment) {
        this.id = id;
        this.ordersList = ordersList;
        this.context = activity;
        this.orderStateFragment = orderStateFragment;
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
            holder.tvOrderCancel_2 = (TextView) convertView.findViewById(R.id.order_cancel_2);
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
      /*  全部===｛0已作废｝、
        1待确认【取消订单】、
        2待付款【代付款】【取消订单】、
        在途跟踪===={3在途跟踪(待发运)【取消订单】、4已发运}、
        已收车===={5待评价【评价】、6已收车(结单)}
        全部====｛7 未支付 8 已经支付 9 待退款 10已退款｝(取消订单的状态)*/
        switch (orderInfoBean.order_status){
            case "1":
                holder.tvOrderState.setText("待确认");
                holder.tvOrderCancel.setText("取消订单");
                holder.tvOrderCancel.setTextColor(CommonUtil.getResourceColor(android.R.color.holo_red_light));
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel_2.setBackgroundResource(R.drawable.rectangle_white_with_red_line_shape);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(position);
                    }
                });
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "2":
                holder.tvOrderState.setText("待付款");
                holder.tvOrderCancel_2.setText("取消订单");
                holder.tvOrderCancel_2.setVisibility(View.VISIBLE);
                holder.tvOrderCancel_2.setBackgroundResource(R.drawable.rectangle_white_with_red_line_shape);
                holder.tvOrderCancel_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(position);
                    }
                });
                holder.tvOrderCancel.setText("  去付款  ");
                holder.tvOrderCancel.setTextColor(CommonUtil.getResourceColor(android.R.color.holo_green_light));
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setBackgroundResource(R.drawable.rectangle_white_with_green_line_shape);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goPay(position);
                    }
                });

                break;
            case "3":
                holder.tvOrderState.setText("待发运");
                holder.tvOrderCancel.setText("申请退款");
                holder.tvOrderCancel.setTextColor(CommonUtil.getResourceColor(android.R.color.holo_green_light));
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setBackgroundResource(R.drawable.rectangle_white_with_green_line_shape);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitRefund(position);
                    }
                });
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "4":
                holder.tvOrderState.setText("已发运");
                holder.tvOrderCancel.setText("查看物流");
                holder.tvOrderCancel.setTextColor(CommonUtil.getResourceColor(android.R.color.holo_green_light));
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setBackgroundResource(R.drawable.rectangle_white_with_green_line_shape);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTransportInfo(position);
                    }
                });
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "5":
                holder.tvOrderState.setText("待评价");
                holder.tvOrderCancel.setText("  去评价  ");
                holder.tvOrderCancel.setTextColor(CommonUtil.getResourceColor(android.R.color.holo_green_light));
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setBackgroundResource(R.drawable.rectangle_white_with_green_line_shape);
                holder.tvOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goComment(position);
                    }
                });
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "6":
                holder.tvOrderState.setText("已收车");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "7":
                holder.tvOrderState.setText("已取消");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "8":
                holder.tvOrderState.setText("已取消");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "9":
                holder.tvOrderState.setText("待退款");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
            case "10":
                holder.tvOrderState.setText("已退款");
                holder.tvOrderCancel.setVisibility(View.INVISIBLE);
                holder.tvOrderCancel_2.setVisibility(View.INVISIBLE);
                break;
        }
        return convertView;
    }

    private void goComment(int position) {

    }

    private void showTransportInfo(int position) {

    }

    private void submitRefund(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("申请退款");//设置对话框标题
        builder.setMessage("您确认申请退款吗？申请退款订单将取消！");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                App.appAction.submitRefundOrder(ordersList.get(position).order_no, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        ToastUtil.showToast(App.app,"取消订单成功！");
                        orderStateFragment.initData();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        ToastUtil.showToast(App.app,message);
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }

    private void goPay(int position) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(PayActivity.OrderID,ordersList.get(position).order_no);
        context.startActivity(intent);
    }

    private void cancelOrder(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("取消订单");//设置对话框标题
        builder.setMessage("您确认取消订单吗？");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                App.appAction.cancelOrder(ordersList.get(position).order_no, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        ToastUtil.showToast(App.app,"取消订单成功！");
                        orderStateFragment.initData();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        ToastUtil.showToast(App.app,message);
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }


    class ViewHolder {
        TextView tvOrderNo;//订单编号
        TextView tvOrderDes;//目的地
        TextView tvorderDep;//出发地
        TextView tvOrderDate;//订单日期
        TextView tvOrderState;//订单状态
        TextView tvOrderCancel;//订单状态
        TextView tvOrderCancel_2;//订单状态
    }
}

