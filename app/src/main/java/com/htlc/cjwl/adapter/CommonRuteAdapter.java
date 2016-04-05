package com.htlc.cjwl.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.AddressInfo;

import java.util.List;

/**
 * Created by luochuan on 15/11/9.
 */
public class CommonRuteAdapter extends BaseAdapter {

    private List<AddressInfo> list;
    private Context context;
    private int mRightWidth = 0;

    public CommonRuteAdapter(List<AddressInfo> list, Context context, int mRightWidth) {
        this.list = list;
        this.context = context;
        this.mRightWidth = mRightWidth;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_common_rute, null);
        RelativeLayout item_right = (RelativeLayout) view.findViewById(R.id.item_right);
        LinearLayout item_left = (LinearLayout) view.findViewById(R.id.ll_left);
        TextView tvCommonToAddress = (TextView) view.findViewById(R.id.tv_common_to_address);
        TextView tvCommonFromAddress = (TextView) view.findViewById(R.id.tv_common_from_address);
        tvCommonFromAddress.setSingleLine(true);
        tvCommonToAddress.setSingleLine(true);
        tvCommonToAddress.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        tvCommonFromAddress.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        TextView tvToMobile = (TextView) view.findViewById(R.id.tv_to_mobile);
        TextView tvToName = (TextView) view.findViewById(R.id.tv_to_name);
        TextView tvFromMobile = (TextView) view.findViewById(R.id.tv_from_mobile);
        TextView tvfromName = (TextView) view.findViewById(R.id.tv_from_name);
        TextView itemRight = (TextView) view.findViewById(R.id.item_right_txt);
        ImageView imgEditor = (ImageView) view.findViewById(R.id.img_common_editor);
        String to_address = list.get(position).getTo_address();
        if(!TextUtils.isEmpty(to_address)) {
            tvCommonToAddress.setText(to_address);
        }else {
            tvCommonToAddress.setText(list.get(position).getTo_cityname());
        }
        String from_address = list.get(position).getFrom_address();
        if(!TextUtils.isEmpty(to_address)) {
            tvCommonFromAddress.setText(from_address);
        }else {
            tvCommonFromAddress.setText(list.get(position).getFrom_cityname());
        }
        tvFromMobile.setText(list.get(position).getFrom_mobile());
        tvfromName.setText(list.get(position).getFrom_name());
        tvToMobile.setText(list.get(position).getTo_mobile());
        tvToName.setText(list.get(position).getTo_name());
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        item_left.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(lp2);
        item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        imgEditor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eListener != null) {
                    eListener.onImageClick(v, position);
                }
            }
        });
        return view;
    }

    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

    private onImageEditorClickListener eListener = null;

    public void setOnImageEditorClickListener(onImageEditorClickListener listener) {
        eListener = listener;
    }

    public interface onImageEditorClickListener {
        void onImageClick(View v, int position);
    }
}
