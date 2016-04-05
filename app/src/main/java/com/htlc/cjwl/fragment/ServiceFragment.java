package com.htlc.cjwl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.ServiceDetailActivity;
import com.htlc.cjwl.activity.WebActivity;
import com.htlc.cjwl.adapter.ServiceListViewAdapter;
import com.htlc.cjwl.bean.ServiceInfoBean;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;

/**
 * Created by Administrator on 2015/10/28.
 */
public class ServiceFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView lv_service_listView;//服务列表的list view
    private ServiceListViewAdapter adapter;//list view的adapter
    private ArrayList<ServiceInfoBean> items = new ArrayList<>();//列表的数据集合

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(this,"onCreateView");
        View view = inflater.inflate(R.layout.layout_service_fragment,null);

        TextView tv_fragment_title = (TextView) view.findViewById(R.id.tv_fragment_title);
        tv_fragment_title.setText(R.string.service_fragment_title);

        lv_service_listView = (ListView) view.findViewById(R.id.lv_service_listView);
        lv_service_listView.setOnItemClickListener(this);
        adapter = new ServiceListViewAdapter(getActivity(), items);
        lv_service_listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i(this, "onActivityCreated");
        initData();
    }

    /**
     * 请求网络数据
     */
    private void initData() {
        App.appAction.serviceList(new ActionCallbackListener<ArrayList<ServiceInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<ServiceInfoBean> data) {
                items.clear();
                items.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }

    /**
     * 条目的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ServiceInfoBean serviceInfoBean = items.get(position);
        LogUtil.i("条目id："+this,serviceInfoBean.id+"；条目类型："+serviceInfoBean.service_type);
        if(Constant.TYPE_SERVICE_HTML.equals(serviceInfoBean.service_type)){
            LogUtil.i(this, "TYPE_SERVICE_HTML");
            Intent intent = new Intent(getActivity(), WebActivity.class);
            String url = String.format(Api.ServiceHtmlDetail, serviceInfoBean.id);
            intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
            intent.putExtra(Constant.SERVICE_DETAIL_TITLE,serviceInfoBean.service_name);
            startActivity(intent);
        }else if(Constant.TYPE_SERVICE_LOCAL.equals(serviceInfoBean.service_type)){
            LogUtil.i(this,"TYPE_SERVICE_LOCAL");
            Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
            intent.putExtra(Constant.SERVICE_DETAIL_ID,serviceInfoBean.id);
            intent.putExtra(Constant.SERVICE_DETAIL_TITLE,serviceInfoBean.service_name);
            startActivity(intent);
        }
    }
}
