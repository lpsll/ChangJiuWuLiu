package com.htlc.cjwl.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.RefundListAdapter;
import com.htlc.cjwl.adapter.RefundListSelectAdapter;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;

/**
 * Created by Larno 2016/04/01;
 */
public class RefundStateFragment extends Fragment {
    private static final String[] RefundFragmentStatus = {"1", "2"};
    private int id;
    private BaseAdapter adapter;
    private ArrayList refundsList = new ArrayList();//某种类型的订单集合
    private PullToRefreshListView listView;
    private RelativeLayout relativeNoData;
    private int page = 1;

    public static RefundStateFragment newInstance(int id) {
        RefundStateFragment fragment = new RefundStateFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_refund_state, null);
        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                if (listView.isShownHeader()) {
                    initData1();
                } else if (listView.isShownFooter()) {//上拉加载
                    getMoreData();
                }
            }
        });
        if(id == 0){
            listView.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            adapter = new RefundListSelectAdapter(id, refundsList, getActivity());
        }else {
            adapter = new RefundListAdapter(id, refundsList, getActivity());
        }
        listView.setAdapter(adapter);
        relativeNoData = (RelativeLayout) view.findViewById(R.id.relativeNoData);
        return view;
    }

    /**
     * 申请退款
     */
    public void submitRefund(){
        long[] checkedItemIds = listView.getRefreshableView().getCheckedItemIds();
        // TODO: 2016/4/7 申请退款
        initData1();
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("客服人员会及时与您沟通，处理退款事宜");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData1();
    }

    private void initData1() {
        for(int i=0; i<listView.getRefreshableView().getCount(); i++){
            listView.getRefreshableView().setItemChecked(i,false);
        }

        refundsList.clear();
        int i = 0;
        while(i<10){
            refundsList.add(Math.random());
            i++;
        }
        adapter.notifyDataSetChanged();
        listView.onRefreshComplete();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initData() {
        page = 1;
        App.appAction.orderList(RefundFragmentStatus[id], page, new ActionCallbackListener<ArrayList<OrderInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<OrderInfoBean> data) {
                refundsList.clear();
                refundsList.addAll(data);
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
        if (refundsList.size() > 0) {
            relativeNoData.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    private void getMoreData() {
        App.appAction.orderList(RefundFragmentStatus[id], page, new ActionCallbackListener<ArrayList<OrderInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<OrderInfoBean> data) {
                refundsList.addAll(data);
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
}