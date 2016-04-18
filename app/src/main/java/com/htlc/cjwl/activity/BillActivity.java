package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.BillListSelectAdapter;
import com.htlc.cjwl.adapter.RefundListSelectAdapter;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.BillOrderBean;
import model.RefundOrderBean;

/**
 * Created by sks on 2016/4/7.
 */
public class BillActivity extends Activity{
    private TextView textTitle,textRightButton;

    private PullToRefreshListView listView;
    private BaseAdapter adapter;
    private ArrayList billList = new ArrayList();//某种类型的订单集合

    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("我的发票");
        textRightButton = (TextView) findViewById(R.id.confirm);
        textRightButton.setText("我要发票");
        textRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBill();
            }
        });

        listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                if (listView.isShownHeader()) {
                    initData();
                } else if (listView.isShownFooter()) {//上拉加载
                    getMoreData();
                }
            }
        });
        listView.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter = new BillListSelectAdapter(-1, billList, this);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void getMoreData() {
        App.appAction.billOrderList(page, new ActionCallbackListener<ArrayList<BillOrderBean>>() {
            @Override
            public void onSuccess(ArrayList<BillOrderBean> data) {
                billList.addAll(data);
                adapter.notifyDataSetChanged();
                page++;
                listView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                listView.onRefreshComplete();
            }
        });
    }

    private void initData() {
        page = 1;
        App.appAction.billOrderList(page, new ActionCallbackListener<ArrayList<BillOrderBean>>() {
            @Override
            public void onSuccess(ArrayList<BillOrderBean> data) {
                billList.clear();
                billList.addAll(data);
                adapter.notifyDataSetChanged();
                page++;
                listView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                listView.onRefreshComplete();
            }
        });
    }

    /**
     * 申请发票界面
     */
    private void submitBill() {
        SparseBooleanArray checkedItemPositions = listView.getRefreshableView().getCheckedItemPositions();
        StringBuilder ordersArray  = new StringBuilder();
        double priceTotal = 0;
        if(checkedItemPositions.size()<1){
            return;
        }
        boolean flag = false;
        for(int i=0; i<checkedItemPositions.size(); i++){
            int key = checkedItemPositions.keyAt(i);
            if(checkedItemPositions.get(key)){
                if(key-1<0){
                    key = 1;
                }
                BillOrderBean bean = (BillOrderBean) billList.get(key-1);
                ordersArray.append(bean.order_no+",");
                priceTotal += Double.parseDouble(bean.order_price);
                flag = true;
            }
        }
        String ordersArrayStr = ordersArray.toString();
        for(int i=0; i<listView.getRefreshableView().getCount(); i++){
            listView.getRefreshableView().setItemChecked(i,false);
        }
        if(flag){
            Intent intent = new Intent(this,SubmitBillActivity.class);
            intent.putExtra(SubmitBillActivity.TotalPrice,priceTotal+"");
            intent.putExtra(SubmitBillActivity.OrderArrayStr,ordersArrayStr);
            startActivity(intent);
        }
    }
}
