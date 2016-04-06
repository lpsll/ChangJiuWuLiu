package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.CarTypeLeftAdapter;
import com.htlc.cjwl.adapter.CarTypeRightAdapter;

import java.util.ArrayList;

import model.CarInfoBean;

/**
 * Created by sks on 2016/4/6.
 */
public class CarTypeActivity extends Activity {
    public static final String SelectCar = "SelectCar";

    private TextView textTitle;
    private ListView listViewLeft,listViewRight;
    private CarTypeLeftAdapter leftAdapter;
    private CarTypeRightAdapter rightAdapter;
    private ArrayList leftList = new ArrayList(),rightList = new ArrayList();
    private ArrayList<CarInfoBean> carArray;
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

    private void leftSelected(int position) {
        leftAdapter.setCheckPosition(position);
        leftAdapter.notifyDataSetChanged();
        rightList.clear();
        getRightData(position);

    }
    private void setResultData(int position) {
        int checkPosition = leftAdapter.getCheckPosition();
        CarInfoBean bean = new CarInfoBean();
        bean.carName = "Left:"+checkPosition+";right:"+position;
        bean.carId = checkPosition+"/"+position;
        for(int i=0;i<carArray.size();i++){
            if(bean.carId.equals(carArray.get(i).carId)){
                finish();
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra(SelectCar, bean);
        setResult(RESULT_OK, data);
        finish();
    }

    private void initData() {
        for(int i=0;i<20;i++){
            leftList.add(true);
        }
        leftAdapter.notifyDataSetChanged();
        getRightData(0);
    }
    private void getRightData(int position) {
        // TODO: 2016/4/6 获取网络数据
        for(int i=0;i<position+1;i++){
            rightList.add(true);
        }
        rightAdapter.notifyDataSetChanged();
    }
}
