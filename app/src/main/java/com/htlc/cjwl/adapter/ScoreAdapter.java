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

import model.RefundOrderBean;
import model.ScoreBean;

/**
 * Created by Larno on 16/04/07.
 */
public class ScoreAdapter extends BaseAdapter{

    private ArrayList<ScoreBean> ordersList;
    private Context context;
    private String TIME_STR = "<font color=\"#3c3c3c\">获取时间:  </font>" +
            "<font color=\"#0a198c\">%1$s</font>";
    public static final String SCORE_STR = "<font color=\"#3c3c3c\">获取值:  </font>" +
            "<font color=\"#0a198c\">%1$s积分</font>";
    public static final String LIMITE_STR = "<font color=\"#3c3c3c\">有效期:  </font>" +
            "<font color=\"#0a198c\">%1$s至%2$s</font>";

    public ScoreAdapter(ArrayList ordersList, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_score, null);
            holder = new ViewHolder();
            holder.textTime = (TextView) convertView.findViewById(R.id.textTime);
            holder.textScore = (TextView) convertView.findViewById(R.id.textScore);
            holder.textLimit = (TextView) convertView.findViewById(R.id.textLimit);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        ScoreBean bean = ordersList.get(position);
        String timeStr = String.format(TIME_STR, bean.starttime);
        holder.textTime.setText(Html.fromHtml(timeStr));
        String scoreStr = String.format(SCORE_STR, bean.value);
        holder.textScore.setText(Html.fromHtml(scoreStr));
        if(bean.starttime.equals(bean.endtime)){
            holder.textLimit.setVisibility(View.GONE);
        }else {
            String limitStr = String.format(LIMITE_STR, bean.starttime.substring(0,10),bean.endtime.substring(0,10));
            holder.textLimit.setText(Html.fromHtml(limitStr));
            holder.textLimit.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    class ViewHolder {
        TextView textScore;
        TextView textLimit;
        TextView textTime;//订单日期
    }
}

