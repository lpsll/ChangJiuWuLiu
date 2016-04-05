package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.HotCitysAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.HotCitysBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.JsonUtil;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/4.
 */
public class NetworkQueryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private GridView gv_grid_view;
    private TextView tv_show_more;

    private ArrayList<CityInfoBean> list;
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


        initData();


    }

    private void initData() {


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
                // TODO: 2015/11/4 跳转到所有城市列表界面
                startActivity(new Intent(this,CityListActivity.class));
                break;


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityInfoBean cityInfoBean = list.get(position);
        LogUtil.i(this,"点击了城市："+ cityInfoBean.cityname+";id为："+cityInfoBean.id);
        Intent intent = new Intent(this, WebActivity.class);
        // TODO: 2015/11/17 添加网点的html页面地址
        String url = String.format("",cityInfoBean.id);
        intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
        intent.putExtra(Constant.SERVICE_DETAIL_TITLE,"网点详情");
        startActivity(intent);
    }
}
