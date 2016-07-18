package com.htlc.cjwl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.BillDetailActivity;
import com.htlc.cjwl.activity.SubmitBillActivity;
import com.htlc.cjwl.adapter.BillListAdapter;
import com.htlc.cjwl.adapter.BillListSelectAdapter;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.BillDetailBean;
import model.BillOrderBean;
import util.ToastUtil;

/**
 * Created by Larno 2016/04/01;
 * 发票Fragment
 */
public class BillStateFragment extends Fragment {
    private int id;
    private BaseAdapter adapter;
    private ArrayList billList = new ArrayList();//某种类型的订单集合
    private PullToRefreshListView listView;
    private RelativeLayout relativeNoData;
    private int page = 1;

    public static BillStateFragment newInstance(int id) {
        BillStateFragment fragment = new BillStateFragment();
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
        View view = inflater.inflate(R.layout.layout_fragment_bill_state, null);
        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
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
        //当Fragment id为0，表示是可申请状态；
        if (id == 0) {
            listView.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            adapter = new BillListSelectAdapter(id, billList, getActivity());
        } else {//id为1，表示是已申请状态
            adapter = new BillListAdapter(id, billList, getActivity());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goBillDetail(position-1);
                }
            });
        }
        listView.setAdapter(adapter);
        relativeNoData = (RelativeLayout) view.findViewById(R.id.relativeNoData);
        return view;
    }

    /**
     * 发票详情
     * @param position
     */
    private void goBillDetail(int position) {
        if(position>=0){
            //1申请中，2已打印
            Intent intent = new Intent(getActivity(), BillDetailActivity.class);
            BillDetailBean bean = (BillDetailBean) billList.get(position);
            if("1".equals(bean.invoice_flag)){
                intent.putExtra(BillDetailActivity.BillStatus, true);
            }else {
                intent.putExtra(BillDetailActivity.BillStatus, false);
            }
            intent.putExtra(BillDetailActivity.BillId, bean.id);
            startActivity(intent);
        }
        
    }

    /**
     * 获取要申请发票的订单，去申请发票界面
     */
    public void submitBill() {
        SparseBooleanArray checkedItemPositions = listView.getRefreshableView().getCheckedItemPositions();
        StringBuilder ordersArray = new StringBuilder();
        double priceTotal = 0;
        if (checkedItemPositions.size() < 1) {
            ToastUtil.showToast(App.app,"请选择要申请的订单！");
            return;
        }
        boolean flag = false;
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            int key = checkedItemPositions.keyAt(i);
            if (checkedItemPositions.get(key)) {
                if (key - 1 < 0) {
                    key = 1;
                }
                BillOrderBean bean = (BillOrderBean) billList.get(key - 1);
                ordersArray.append(bean.order_no + ",");
                priceTotal += Double.parseDouble(bean.order_price);
                flag = true;
            }
        }
        ordersArray.deleteCharAt(ordersArray.lastIndexOf(","));
        String ordersArrayStr = ordersArray.toString();
        for (int i = 0; i < listView.getRefreshableView().getCount(); i++) {
            listView.getRefreshableView().setItemChecked(i, false);
        }
        if (flag) {
            Intent intent = new Intent(getActivity(), SubmitBillActivity.class);
            intent.putExtra(SubmitBillActivity.TotalPrice, priceTotal + "");
            intent.putExtra(SubmitBillActivity.OrderArrayStr, ordersArrayStr);
            startActivity(intent);
        }else{

        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    /*根据Fragment id，或可申请发票或历史发票列表*/
    public void initData() {
        if(id == 0){
            initBillList();
        }else {
            initBillListHistory();
        }
    }

    private void initBillListHistory() {
        page = 1;
        App.appAction.billOrderListHistory(page, new ActionCallbackListener<ArrayList<BillDetailBean>>() {
            @Override
            public void onSuccess(ArrayList<BillDetailBean> data) {
                billList.clear();
                billList.addAll(data);
                adapter.notifyDataSetChanged();
                page++;
                listView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                billList.clear();
                adapter.notifyDataSetChanged();
                ToastUtil.showToast(App.app, message);
                listView.onRefreshComplete();
            }
        });
    }

    private void initBillList() {
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
                billList.clear();
                adapter.notifyDataSetChanged();
                ToastUtil.showToast(App.app, message);
                listView.onRefreshComplete();
            }
        });
    }

    private void refreshView() {
        if (billList.size() > 0) {
            relativeNoData.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        } else {
            relativeNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    private void getMoreData() {
        if(id == 0){
            getMoreBillList();
        }else {
            getMoreHistory();
        }
    }

    private void getMoreHistory() {
        App.appAction.billOrderListHistory(page, new ActionCallbackListener<ArrayList<BillDetailBean>>() {
            @Override
            public void onSuccess(ArrayList<BillDetailBean> data) {
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

    private void getMoreBillList() {
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


}