package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.CarTypeLeftAdapter;
import com.htlc.cjwl.adapter.CarTypeRightAdapter;

import util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.CarInfoBean;
import model.CarTypeInfoBean;

/**
 * Created by sks on 2016/4/6.
 * 汽车品牌型号Acitivity
 */
public class CarTypeActivity extends Activity {
    public static final String SelectCar = "SelectCar";

    private TextView textTitle;
    private ListView listViewLeft, listViewRight;//品牌ListView和型号ListView
    private CarTypeLeftAdapter leftAdapter;//品牌Adapter
    private CarTypeRightAdapter rightAdapter;//型号Adapter
    private ArrayList leftList = new ArrayList(), rightList = new ArrayList();//品牌数据集合
    private ArrayList<CarInfoBean> carArray;//选择的汽车数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type);
        carArray = getIntent().getParcelableArrayListExtra(SelectCar);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("请选择品牌/车型");

        listViewLeft = (ListView) findViewById(R.id.listViewLeft);
        listViewRight = (ListView) findViewById(R.id.listViewRight);
        leftAdapter = new CarTypeLeftAdapter(this, leftList);
        listViewLeft.setAdapter(leftAdapter);
        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftSelected(position);
            }
        });
        rightAdapter = new CarTypeRightAdapter(this, rightList);
        listViewRight.setAdapter(rightAdapter);
        listViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResultData(position);
            }
        });
        initData();
    }

    /*品牌列表的点击事件，点击后加载对应的型号列表*/
    private void leftSelected(int position) {
        leftAdapter.setCheckPosition(position);
        leftAdapter.notifyDataSetChanged();
        rightList.clear();
        getRightData(position);

    }

    /*型号列表的点击事件，点击后返回选择的汽车数组*/
    private void setResultData(int position) {
        int checkPosition = leftAdapter.getCheckPosition();
        CarTypeInfoBean carTypeInfoBean = (CarTypeInfoBean) rightList.get(position);
        CarInfoBean bean = new CarInfoBean();
        bean.name = carTypeInfoBean.car_name;
        bean.id = carTypeInfoBean.id;
        bean.price = carTypeInfoBean.car_price;
        bean.type = carTypeInfoBean.car_type;
        for (int i = 0; i < carArray.size(); i++) {
            if (bean.id.equals(carArray.get(i).id)) {
                finish();
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra(SelectCar, bean);
        setResult(RESULT_OK, data);
        finish();
    }

    /*获取品牌列表*/
    private void initData() {
        App.appAction.carTypeList(new ActionCallbackListener<ArrayList<CarTypeInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<CarTypeInfoBean> data) {
                leftList.clear();
                leftList.addAll(data);
                leftAdapter.notifyDataSetChanged();
                getRightData(0);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });

    }

    /*根据品牌获取，车型列表*/
    private void getRightData(int position) {
        rightList.clear();
        rightAdapter.notifyDataSetChanged();
        String carType = ((CarTypeInfoBean) leftList.get(position)).car_brand;
        App.appAction.carNameList(carType, new ActionCallbackListener<ArrayList<CarTypeInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<CarTypeInfoBean> data) {
                rightList.addAll(data);
                rightAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                rightAdapter.notifyDataSetChanged();
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
