package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.widget.PickerView;

import java.util.ArrayList;
import java.util.List;

import model.CarInfoBean;


/**
 * Created by larno on 15/11/3.
 * 设置每种车型的数量
 */
public class SelectCarNumActivity extends Activity implements View.OnClickListener {
    public static final String SelectCarNumWithType = "SelectCarNumWithType";

    private View rootView;
    private TextView textTitle;//标题
    private LinearLayout linearCarNumContainer;//汽车列表的容器
    private ArrayList<CarInfoBean> carArray;//汽车数组

    private int currentPosition;//当前选择的位置
    private String currentNum;//当前选择的数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_select_car_num, null);
        setContentView(rootView);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultData();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("选择数量");
        linearCarNumContainer = (LinearLayout) findViewById(R.id.linearCarNumContainer);
        carArray = getIntent().getParcelableArrayListExtra(SelectCarNumWithType);
        initView();
    }

    /*返回设置汽车数量后的汽车数组*/
    private void setResultData() {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(SelectCarNumWithType,carArray);
        setResult(RESULT_OK, data);
        finish();
    }

    /*初始化汽车列表*/
    private void initView() {
        for (int i = 0; i < carArray.size(); i++) {
            Log.e("Car",carArray.get(i).name +";num="+carArray.get(i).num);
            LinearLayout linearLayout = (LinearLayout) View.inflate(this,R.layout.layout_select_car_num,null);
            TextView textCarName = (TextView) linearLayout.findViewById(R.id.textCarName);
            textCarName.setText(carArray.get(i).name);
            TextView textCarNum = (TextView) linearLayout.findViewById(R.id.textCarNum);
            textCarNum.setText(carArray.get(i).num);
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(this);
            linearCarNumContainer.addView(linearLayout);
        }
    }


    @Override
    public void onClick(View v) {
        currentPosition = (Integer) v.getTag();
        popWindow();
    }


    /*选择汽车数量对话框*/
    private void popWindow() {
        View view = View.inflate(SelectCarNumActivity.this, R.layout.pop_number_selector, null);
        TextView ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setAnimationStyle(R.style.popWindow_animation);
        linearCarNumContainer.setAlpha(0.7f);
        pw.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        //点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                    linearCarNumContainer.setAlpha(1f);
                }
                return false;
            }
        });
        final PickerView pvNum = (PickerView) view.findViewById(R.id.pv_num);
        List<String> data = new ArrayList<String>();

        data.add("99");
        for (int i = 1; i < 99; i++) {
            data.add("" + i);
        }
        pvNum.setData(data);
        pvNum.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String num) {
                currentNum = num;
            }
        });
        currentNum = carArray.get(currentPosition).num;
//        pvNum.setSelected(data.indexOf(currentNum));
        pvNum.setSelected(1);
        currentNum = "1";
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textNum = (TextView) linearCarNumContainer.getChildAt(currentPosition).findViewById(R.id.textCarNum);
                textNum.setText(currentNum);
                carArray.get(currentPosition).num = currentNum;
                linearCarNumContainer.setAlpha(1f);
                pw.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCarNumContainer.setAlpha(1f);
                pw.dismiss();
            }
        });

    }
}
