package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.MessageCenterAdapter;
import com.htlc.cjwl.bean.MessageCenterBean;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/2.
 */
public class MessageCenterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView> {
    private TextView tv_activity_title;
    private ImageView iv_back;

    private PullToRefreshListView lv_message_list;//带下拉刷新的list view
    private MessageCenterAdapter adapter;//list view 的adapter
    private MessageCenterBean bean;
    //    private boolean isRefreshing = false;
    private int pageIndex = 1;//当前消息的页码
    private ArrayList<MessageInfoBean> messageList = new ArrayList<MessageInfoBean>();//消息集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_message_list = (PullToRefreshListView) findViewById(R.id.lv_message_list);


        tv_activity_title.setText(R.string.message_center);
        iv_back.setOnClickListener(this);
        //设置list view的条目点击事件
        lv_message_list.setOnItemClickListener(this);
        //设置下拉刷新模式，即下拉又可以上拉
        lv_message_list.setMode(PullToRefreshBase.Mode.BOTH);
//        lv_message_list.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
//        lv_message_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
//        lv_message_list.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        //设置刷新监听
        lv_message_list.setOnRefreshListener(this);
        initData();


    }

    /**
     * 从服务器获取数据，加载第一页数据
     */
    private void initData() {
        pageIndex = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * list view 条目点击事件，点击开启web Activity，传入网页的地址和标题
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemId = messageList.get(position-1).id;
        String itemTitle = messageList.get(position-1).title;
        LogUtil.i(this, "点击了条目位置：" + position + "点击了条目id：" + itemId + ";title:+" + itemTitle);

        String url = "aa";
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
        intent.putExtra(Constant.SERVICE_DETAIL_TITLE, itemTitle);
        startActivity(intent);
    }

    /**
     * 刷新监听，对刷新操作的处理
     * @param refreshView
     */
    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        LogUtil.i(this, "刷新。。。。。。。。。。:" + lv_message_list.isRefreshing());
        //下拉刷新
        if (lv_message_list.isShownHeader()) {
            LogUtil.i(this, "pull-to-refresh");
            initData();
        } else if (lv_message_list.isShownFooter()) {//上拉加载
            LogUtil.i(this, "pull-to-load-more");
            loadNextPage();
        }

    }

    /**
     * 加载第pageIndex页
     */
    private void loadNextPage() {
    }
}
