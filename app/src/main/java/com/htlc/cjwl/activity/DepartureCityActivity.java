package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.HotcityAdapter;
import com.htlc.cjwl.bean.HotCity;

import java.util.List;


/**
 * Created by luochuan on 15/11/3.
 */
public class DepartureCityActivity extends AppCompatActivity {
    private List<HotCity> list;
    private HotcityAdapter adapter;
    private int pointer = -1;//城市选中标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_city);
    }
}
