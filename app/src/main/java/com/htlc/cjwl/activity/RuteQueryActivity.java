package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.RouteQueryAdapter;
import com.htlc.cjwl.bean.RouteQueryFootInfoBean;
import util.LogUtil;
import util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2015/11/4.
 */
public class RuteQueryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private TextView select_city_begin;//显示出发城市
    private TextView destilation_city;//显示出发城市
    private TextView tv_confirm;//查询控件
    private TextView tv_query_result;//查询结果显示控件
    private ViewPager vp_route_foot;//底部轮播图
    private ArrayList<RouteQueryFootInfoBean> list;//轮播图数据
    private RouteQueryAdapter adapter;//轮播图适配器

    String startCityName;
    String startCityId;
    String endCityName;
    String endCityId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute_query);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        select_city_begin = (TextView) findViewById(R.id.select_city_begin);
        destilation_city = (TextView) findViewById(R.id.destilation_city);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);
        vp_route_foot = (ViewPager) findViewById(R.id.vp_route_foot);

        tv_activity_title.setText(R.string.route_query);

        iv_back.setOnClickListener(this);
        select_city_begin.setOnClickListener(this);
        destilation_city.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);


        initData();

    }

    private void initData() {


        //请求底部轮播图的数据



    }

    /**
     * 调用接口，根据start和end城市，查询路线
     */
    private void queryRoute() {
        if(TextUtils.isEmpty(startCityId)){
            ToastUtil.showToast(this,"请选择出发城市！");
            return;
        }
        if(TextUtils.isEmpty(endCityId)){
            ToastUtil.showToast(this,"请选择目的城市！");
            return;
        }
        String url = String.format("url",startCityId,endCityId);



    }

    /**
     * Intent intent = getIntent();
         int tag = intent.getIntExtra("TAG", 0);
         if(tag==1){
         title.setText("选择目的城市");
         }else {
         title.setText("选择出发城市");
         }

     Intent intent = new Intent();
     intent.putExtra("KEY_TEST", list.get(pointer).getCityname());
     intent.putExtra("ID",list.get(pointer).getId());
     setResult(RESULT_OK, intent);
     this.finish();
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.select_city_begin:
                LogUtil.i(this, "select_city_begin");
                Intent intent1 = new Intent(this, SelectCityForAddressActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.destilation_city:
                LogUtil.i(this, "destilation_city");
                Intent intent2 = new Intent(this, SelectCityForAddressActivity.class);
                startActivityForResult(intent2,1);
                break;
            case R.id.tv_confirm:
                LogUtil.i(this, "query");
                queryRoute();
                break;
        }
    }

    /**
     * 对开启城市列表Activity，返回结果的处理；获取出发、目的城市
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(this,"获得start或end城市的数据");
        if(data==null){
            LogUtil.i(this,"没有选择城市");
            return;
        }
        if(requestCode==0){
            LogUtil.i(this,"出发城市："+data.getStringExtra("KEY_TEST"));
            startCityId = data.getStringExtra("ID");
            startCityName = data.getStringExtra("KEY_TEST");
            select_city_begin.setText(startCityName);
        }else if(requestCode==1){
            LogUtil.i(this,"出发城市："+data.getStringExtra("KEY_TEST"));
            endCityId = data.getStringExtra("ID");
            endCityName = data.getStringExtra("KEY_TEST");
            destilation_city.setText(endCityName);
        }
    }
}
