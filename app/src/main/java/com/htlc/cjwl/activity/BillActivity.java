package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.RefundListSelectAdapter;

import java.util.ArrayList;

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
        adapter = new RefundListSelectAdapter(-1, billList, this);
        listView.setAdapter(adapter);
        initData();
    }

    private void getMoreData() {

    }

    private void initData() {
        int i = 10;
        while (i>0){
            billList.add(Math.random());
            i--;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 申请发票界面
     */
    private void submitBill() {
        for(int i=0; i<listView.getRefreshableView().getCount(); i++){
            listView.getRefreshableView().setItemChecked(i,false);
        }
        Intent intent = new Intent(this,SubmitBillActivity.class);
        startActivity(intent);
    }
}
