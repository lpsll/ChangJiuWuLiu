package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.fragment.CityListFragment;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;

/**
 * Created by sks on 2015/11/4.
 * 该Activity有：一个标题，一个fragment
 */
public class CityListActivity  extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.more_network);
        iv_back.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CityListFragment fragment = new CityListFragment();
        fragment.setFlag(Constant.TYPE_FRAGMENT_CITY_LIST_MORE);
        fragmentTransaction.replace(R.id.fl_container, fragment);
        fragmentTransaction.commit();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;



        }
    }


}
