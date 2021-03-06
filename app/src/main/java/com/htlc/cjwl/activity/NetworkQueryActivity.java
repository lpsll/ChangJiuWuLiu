package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.HotCitysAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.util.Constant;
import util.LogUtil;
import util.ToastUtil;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/4.
 */
public class NetworkQueryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private GridView gv_grid_view;//热门城市列表
    private TextView tv_show_more;//查看更多按钮

    private ArrayList<CityInfoBean> list = new ArrayList<>();
    private HotCitysAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_query);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        gv_grid_view = (GridView) findViewById(R.id.gv_grid_view);
        tv_show_more = (TextView) findViewById(R.id.tv_show_more);

        tv_activity_title.setText(R.string.network_query);

        iv_back.setOnClickListener(this);
        tv_show_more.setOnClickListener(this);
        gv_grid_view.setOnItemClickListener(this);

        adapter = new HotCitysAdapter(list);
        gv_grid_view.setAdapter(adapter);
        initData();
    }

    /*获取热门城市*/
    private void initData() {
        App.appAction.hotCity(new ActionCallbackListener<ArrayList<CityInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<CityInfoBean> data) {
                list.clear();
                list.addAll(data);
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
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_show_more:
                LogUtil.i(this, "tv_show_more");
                startActivity(new Intent(this,CityListActivity.class));
                break;
        }
    }

    /*网点详情HTML界面*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityInfoBean cityInfoBean = list.get(position);
        LogUtil.i(this,"点击了城市："+ cityInfoBean.cityname+";id为："+cityInfoBean.id);
        Intent intent = new Intent(this, WebActivity.class);
        String url = String.format(Api.CityHtmlDetail,cityInfoBean.id);
        intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
        intent.putExtra(Constant.SERVICE_DETAIL_TITLE,"网点详情");
        startActivity(intent);
    }
}
