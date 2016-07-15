package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.TraceAdapter;

import util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.TraceBean;

/**
 * Created by sks on 2016/4/19.
 * 物流信息界面
 */
public class TraceActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String OrderId = "OrderId";
    private String orderId;
    private TextView tv_activity_title;
    private ImageView iv_back;

    private ListView lv_service_listView;//服务列表的list view
    private TraceAdapter adapter;//list view的adapter
    private ArrayList<TraceBean> items = new ArrayList<>();//列表的数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        orderId = getIntent().getStringExtra(OrderId);

        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText("物流信息");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        lv_service_listView = (ListView)findViewById(R.id.lv_listView);
        adapter = new TraceAdapter(this, items);
        lv_service_listView.setAdapter(adapter);

        initData();
    }

    private void initData() {
//        int i=50;
//        while (i>0){
//            TraceBean object = new TraceBean();
//            object.order_address ="新郑适当分离焦虑xxx仓库";
//            object.order_date ="2015-16-17";
//            items.add(object);
//            i--;
//        }
//        adapter.notifyDataSetChanged();
        App.appAction.traceOrder(orderId, new ActionCallbackListener<ArrayList<TraceBean>>() {
            @Override
            public void onSuccess(ArrayList<TraceBean> data) {
                items.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
