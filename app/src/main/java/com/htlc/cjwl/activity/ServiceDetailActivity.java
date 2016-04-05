package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.ServiceDetailBean;
import com.htlc.cjwl.bean.ServiceDetailInfoBean;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.JsonUtil;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.LoginUtil;
import com.htlc.cjwl.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/4.
 */
public class ServiceDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private ImageView iv_service_detail;//服务详情界面的图片
    private TextView tv_service_detail_des;//详情页的描述
    private TextView tv_service_detail_orders;//下单按钮

    private ServiceDetailInfoBean serviceDetailBean;
    private String service_detail_id;//该详情页，对应数据的id
    private String service_detail_title;//该详情页，对应数据的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        service_detail_id = getIntent().getStringExtra(Constant.SERVICE_DETAIL_ID);
        service_detail_title = getIntent().getStringExtra(Constant.SERVICE_DETAIL_TITLE);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        iv_service_detail = (ImageView) findViewById(R.id.iv_service_detail);
        tv_service_detail_des = (TextView) findViewById(R.id.tv_service_detail_des);
        tv_service_detail_orders = (TextView) findViewById(R.id.tv_service_detail_orders);

//        tv_activity_title.setText(R.string.detail);
        tv_activity_title.setText(service_detail_title);

        iv_back.setOnClickListener(this);
        tv_service_detail_orders.setOnClickListener(this);

        initData();

    }

    /**
     * 根据Service detail Id请求数据
     */
    private void initData() {
       App.appAction.serviceLocalDetail(service_detail_id, new ActionCallbackListener<ServiceDetailInfoBean>() {
           @Override
           public void onSuccess(ServiceDetailInfoBean data) {
                refreshView(data);
           }

           @Override
           public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
           }
       });

    }

    private void refreshView(ServiceDetailInfoBean data) {
        serviceDetailBean = data;
        ImageLoader.getInstance().displayImage(data.service_subimages,iv_service_detail);
        tv_service_detail_des.setText(data.service_desc);
    }

    /**
     * 下单按钮的事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_service_detail_orders:
                LogUtil.i(this, "处理下单操作");
                goOrder();
                break;
        }
    }

    private void goOrder() {
        //判断用户是否登录
        if(serviceDetailBean != null){
            if(!LoginUtil.isOnline()){
                startActivity(new Intent(this,LoginActivity.class));
            }else{
                startActivity(new Intent(this,OrderInfoActivity.class));
            }
        }
    }
}
