package com.htlc.cjwl.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;

/**
 * Created by sks on 2016/4/7.
 */
public class RefundListSelectItem extends LinearLayout implements Checkable {
    public TextView textTime, textOrderId, textPrice;
    public CheckBox checkbox;

    public RefundListSelectItem(Context context) {
        this(context, null);
    }

    public RefundListSelectItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefundListSelectItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(Color.WHITE);
        this.addView(View.inflate(context, R.layout.adapter_refund_list_select, null));
        textTime = (TextView) findViewById(R.id.textTime);
        textOrderId = (TextView) findViewById(R.id.textOrderId);
        textPrice = (TextView) findViewById(R.id.textPrice);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefundListSelectItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkbox != null) {
            checkbox.setChecked(checked);
        }

    }

    @Override
    public boolean isChecked() {
        return checkbox.isChecked();
    }

    @Override
    public void toggle() {
        setChecked(!checkbox.isChecked());
    }
}
