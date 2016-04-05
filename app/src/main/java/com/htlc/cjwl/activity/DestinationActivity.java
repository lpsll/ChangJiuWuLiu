package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.HotcityAdapter;
import com.htlc.cjwl.bean.HotCity;
import com.htlc.cjwl.bean.SetOutCityBean;

import java.util.List;

/**
 * Created by luochuan on 15/11/9.
 * 留做扩展用
 */
public class DestinationActivity extends AppCompatActivity {
    private SetOutCityBean setOutCityBean;
    private List<HotCity> list;
    private LinearLayout checkedCity;
    private HotcityAdapter adapter;
    private int pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_city);
    }
}
