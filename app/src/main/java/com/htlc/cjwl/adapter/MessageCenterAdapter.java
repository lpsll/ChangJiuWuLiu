package com.htlc.cjwl.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 */
public class MessageCenterAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<MessageInfoBean> list;
    public MessageCenterAdapter(ArrayList<MessageInfoBean> list,Activity activity) {
        this.list = list;
        this.activity = activity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(CommonUtil.getApplication(), R.layout.adapter_message_list,null);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.tv_message_title = (TextView) convertView.findViewById(R.id.tv_message_title);
            holder.tv_message_des = (TextView) convertView.findViewById(R.id.tv_message_des);
            holder.tv_publish_time = (TextView) convertView.findViewById(R.id.tv_publish_time);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final MessageInfoBean bean = list.get(position);
        //添加删除按钮的点击事件
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(position);

            }
        });
        holder.tv_message_title.setText(bean.title);

        String content = bean.content;
        if(content.length()>38){
            content = content.substring(0,38) + "······";
        }
        holder.tv_message_des.setText(content);
        holder.tv_publish_time.setText(bean.publishtime);
        return convertView;
    }



    class ViewHolder{
        TextView tv_message_title;
        TextView tv_message_des;
        TextView tv_publish_time;
        ImageView iv_delete;
    }
    private void requestData(final int position){
        App.appAction.messageDelete(list.get(position).id, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                list.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }
    /**
     * 确认对话框
     */
    public void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        View view = View.inflate(getActivity(),R.layout.layout_dialog_confirm,null);
//        builder.setView(view);
//        builder.show();
        builder.setTitle("删除");//设置对话框标题
        builder.setMessage("您确定删除该消息吗？");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                requestData(position);
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
}
