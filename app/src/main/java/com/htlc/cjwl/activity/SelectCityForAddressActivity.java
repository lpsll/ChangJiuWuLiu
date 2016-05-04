package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.HotcityAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.fragment.CityListFragment2;
import com.htlc.cjwl.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import core.ActionCallbackListener;
import util.ToastUtil;


/**
 * Created by Larno on 16/04/06.
 */
public class SelectCityForAddressActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int SelectFromAddress = 1;
    public static final int SelectToAddress = 2;
    public static final String SelectAddressType = "SelectAddressType";
    public static final String SelectCityName = "SelectCityName";
    public static final String SelectCityID = "SelectCityID";
    private ArrayList<CityInfoBean> list = new ArrayList<>();
    private HotcityAdapter adapter;
    private int pointer = -1;//城市选中标签
    private TextView textTitle;
    private GridView gridView;
    private boolean isTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_city);
        Intent intent = getIntent();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectAddress();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        int selectAddressType = intent.getIntExtra(SelectAddressType, 1);
        isTo = selectAddressType == SelectToAddress;
        textTitle.setText(isTo ? "选择目的城市" : "选择出发城市");

        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.grid_view_hot_city);
        adapter = new HotcityAdapter(this, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        CityListFragment2 cityListFragment = new CityListFragment2();
        cityListFragment.setFlag(!isTo);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container2, cityListFragment);
        fragmentTransaction.commit();

        initData();
    }

    private void initData() {
        App.appAction.cityListForAddress(!isTo, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String jsonArray = jsonObject.getString("hotCitys");
                    ArrayList<CityInfoBean> array = (ArrayList<CityInfoBean>) JsonUtil.parseJsonToList(jsonArray,
                            new TypeToken<ArrayList<CityInfoBean>>() {
                            }.getType());
                    list.clear();
                    list.addAll(array);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSeclection(position);
        pointer = position;
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置选择的城市
     */
    private void setSelectAddress() {
        Intent intent = new Intent();
        if (pointer != -1) {
            intent.putExtra(SelectCityName, list.get(pointer).cityname);
            intent.putExtra(SelectCityID, list.get(pointer).id);
            setResult(RESULT_OK, intent);
        }
        this.finish();
    }

}
