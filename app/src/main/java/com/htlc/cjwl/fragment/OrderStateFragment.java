package com.htlc.cjwl.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.LoginActivity;
import com.htlc.cjwl.activity.OrderDetailActivity;
import com.htlc.cjwl.activity.OrderInfoActivity;
import com.htlc.cjwl.adapter.PullOrderAdapter;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.LoginUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;

/**
 * Created by Larno 2016/04/01;
 */
public class OrderStateFragment extends Fragment implements App.OnLoginListener, AdapterView.OnItemClickListener {
    private static final String[] OrderStatus = {"all","1","2","3","5"};
    private int id;
    private PullOrderAdapter adapter;
    private ArrayList<OrderInfoBean> ordersList = new ArrayList<OrderInfoBean>();//某种类型的订单集合
    private PullToRefreshListView listView;
    private RelativeLayout relativeNoOrder;
    private View tv_goto_shopping;
    private int page = 1;
    public static OrderStateFragment newInstance(int id) {
        OrderStateFragment fragment = new OrderStateFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加登录监听，监听登录是否成功
        CommonUtil.getApplication().addLoginListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtil.getApplication().removeLoginListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_order_state, null);
        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                if (listView.isShownHeader()) {
                    LogUtil.e(OrderStateFragment.this,"initData");
                    initData();
                } else if (listView.isShownFooter()) {//上拉加载
                    LogUtil.e(OrderStateFragment.this,"getMoreData");
                    getMoreData();
                }
            }
        });
        adapter = new PullOrderAdapter(id, ordersList, getActivity(),this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        relativeNoOrder = (RelativeLayout) view.findViewById(R.id.relativeNoOrder);
        tv_goto_shopping = view.findViewById(R.id.tv_goto_shopping);
        tv_goto_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goShopping();
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("OrderStateFragment", "id:" + id + ";onActivityCreated");
//        refreshData();
    }

    private void refreshData() {
        if(LoginUtil.isOnline()){
            initData();
        }else {
            ordersList.clear();
            refreshView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
    }

    public void initData() {
        page = 1;
        App.appAction.orderList(OrderStatus[id], page, new ActionCallbackListener<ArrayList<OrderInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<OrderInfoBean> data) {
                ordersList.clear();
                ordersList.addAll(data);
                adapter.notifyDataSetChanged();
                page++;
                refreshView();
                listView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                refreshView();
                listView.onRefreshComplete();
            }
        });
    }

    private void refreshView() {
        if(ordersList.size()>0){
            relativeNoOrder.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }else {
            relativeNoOrder.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    private void getMoreData() {
        App.appAction.orderList(OrderStatus[id], page, new ActionCallbackListener<ArrayList<OrderInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<OrderInfoBean> data) {
                ordersList.addAll(data);
                adapter.notifyDataSetChanged();
                page++;
                listView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
                listView.onRefreshComplete();
            }
        });
    }

    private void goShopping() {
        if (!LoginUtil.isOnline()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            startActivity(new Intent(getActivity(), OrderInfoActivity.class));
        }
    }

    @Override
    public void onLoginSuccess() {
        refreshData();
    }

    @Override
    public void onLoginError(String msg) {
        refreshData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("OrderList","position="+position);
        position--;
        if(position<0){
            position = 0;
        }
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.OrderId, ordersList.get(position).order_no);
        startActivity(intent);
    }
}