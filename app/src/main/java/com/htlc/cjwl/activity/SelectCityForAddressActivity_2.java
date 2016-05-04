package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.CitysListAdapter;
import com.htlc.cjwl.adapter.HotcityAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import core.ActionCallbackListener;
import util.LogUtil;
import util.ToastUtil;


/**
 * Created by Larno on 16/04/06.
 */
public class SelectCityForAddressActivity_2 extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
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

    private ListView lv_city_list;//城市列表的list view
    private LinearLayout ll_quick_index;//城市列表 左边的 快速索引条

    private ArrayList<String> quickIndexList = new ArrayList<String>();//快速索引条要展示的内容的集合（a,b,c...)
    private ArrayList<String> indexPositionList = new ArrayList<String>();//用于获得点击快速索引的每个条目的位置（如果点击b，从该集合中取 indexOf("b"）
    private ArrayList<CityInfoBean> list_all = new ArrayList<CityInfoBean>();//城市列表信息的集合
    private CitysListAdapter adapter_all;//list view的adapter
    private boolean isTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_city_1);
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

        lv_city_list = (ListView) findViewById(R.id.lv_city_list);
        ll_quick_index = (LinearLayout) findViewById(R.id.ll_quick_index);
        lv_city_list.setOnItemClickListener(this);

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

                    String jsonArrayAll = jsonObject.getString("list");
                    parseJson(jsonArrayAll);
                    initQickIndex();
                    adapter_all = new CitysListAdapter(list_all);
                    lv_city_list.setAdapter(adapter_all);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 将获取的需要在快速索引条中展示的进行展示
     */
    private void initQickIndex() {
        for (String index : quickIndexList) {
            LogUtil.i(this, "qickIndex:" + index);
            TextView index_text_view = (TextView) View.inflate(this, R.layout.layout_quick_index_text_view, null);
            index_text_view.setText(index);
            ll_quick_index.addView(index_text_view);
            index_text_view.setOnClickListener(this);
        }
    }

    /**
     * 解析json数据
     *
     * @param data
     * @throws JSONException
     */
    public void parseJson(String data) throws JSONException {
        JSONObject jsonCity = new JSONObject(data);
        Iterator<String> iterator = jsonCity.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            quickIndexList.add(key);
            LogUtil.i(this, "key:" + key);
            String value = jsonCity.getString(key);
            List<CityInfoBean> citys = (List<CityInfoBean>) JsonUtil.parseJsonToList(value, new TypeToken<List<CityInfoBean>>() {
            }.getType());
            for (int i = 0; i < citys.size(); i++) {
                citys.get(i).group = key;
                indexPositionList.add(key);
            }
            list_all.addAll(citys);
            Collections.sort(list_all);
            Collections.sort(indexPositionList);
            Collections.sort(quickIndexList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == lv_city_list) {
            Intent intent = new Intent();
            intent.putExtra(SelectCityForAddressActivity_2.SelectCityName, list.get(position).name);
            intent.putExtra(SelectCityForAddressActivity_2.SelectCityID, list.get(position).id);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            adapter.setSeclection(position);
            pointer = position;
            adapter.notifyDataSetChanged();
        }
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

    /**
     * 快速索引条的点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String index = ((TextView) v).getText().toString();
            int position = indexPositionList.indexOf(index);
            LogUtil.i(this, "索引:" + index);
            LogUtil.i(this, "位置:" + position + ";城市组：" + list.get(position).group);
            if (adapter != null) {
                lv_city_list.setSelection(position);
            }
        }
    }
}
