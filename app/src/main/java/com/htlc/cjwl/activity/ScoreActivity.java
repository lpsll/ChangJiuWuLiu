package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.htlc.cjwl.adapter.ScoreAdapter;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.BillOrderBean;

/**
 * Created by sks on 2016/4/7.
 */
public class ScoreActivity extends Activity{
    private TextView textTitle,textRightButton;

    private PullToRefreshListView listView;
    private BaseAdapter adapter;
    private ArrayList billList = new ArrayList();//某种类型的订单集合

    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("积分");
        textRightButton = (TextView) findViewById(R.id.confirm);
        textRightButton.setText("积分规则");
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
        adapter = new ScoreAdapter(billList, this);
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

    }
}
