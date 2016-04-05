package com.htlc.cjwl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.htlc.cjwl.R;

/**
 * Created by luochuan on 15/10/30.
 */
public class UnconferListAdapter extends BaseAdapter{
    private Context context;
    public UnconferListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        convertView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_order_item,null);
        return convertView;
    }

    class ViewHolder{

        TextView order_no;
        TextView order_destination;
        TextView order_departure;
        TextView order_date;
        TextView order_state;

    }


}
