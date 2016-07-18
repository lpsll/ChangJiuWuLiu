package com.htlc.cjwl.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.SelectCityForAddressActivity;
import com.htlc.cjwl.activity.WebActivity;
import com.htlc.cjwl.adapter.CitysListAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.JsonUtil;
import util.LogUtil;
import util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import api.Api;
import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/5.
 * 城市列表的fragment,在网点查询中使用
 */
public class CityListFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ListView lv_city_list;//城市列表的list view
    private LinearLayout ll_quick_index;//城市列表 左边的 快速索引条

    private ArrayList<String> quickIndexList = new ArrayList<String>();//快速索引条要展示的内容的集合（a,b,c...)
    private ArrayList<String> indexPositionList = new ArrayList<String>();//用于获得点击快速索引的每个条目的位置（如果点击b，从该集合中取 indexOf("b"）
    private ArrayList<CityInfoBean> list = new ArrayList<CityInfoBean>();//城市列表信息的集合
    private CitysListAdapter adapter;//list view的adapter
    private int flag;//用于标记该fragment在网点查询中使用，还是在 添加地址中使用

    /**
     *  Constant.TYPE_FRAGMENT_CITY_LIST_MORE 表示 网点查询
     *  Constant.TYPE_FRAGMENT_CITY_LIST_HOT 表示 添加地址
     * @param flag
     */
    public void setFlag(int flag){
        this.flag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_city_list, null);
        lv_city_list = (ListView) view.findViewById(R.id.lv_city_list);
        ll_quick_index = (LinearLayout) view.findViewById(R.id.ll_quick_index);
        lv_city_list.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 获取城市列表的数据，并解析
     */
    private void initData() {
        App.appAction.cityList(new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObj = new JSONObject(data);
                    parseJson(jsonObj);
                    initQickIndex();
                    adapter = new CitysListAdapter(list);
                    lv_city_list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    /**
     * 将获取的需要在快速索引条中展示的进行展示
     */
    private void initQickIndex() {
        for(String index : quickIndexList){
            LogUtil.i(this,"qickIndex:"+index);
            if(getActivity()==null){
                return;
            }
            TextView index_text_view = (TextView) View.inflate(getActivity(), R.layout.layout_quick_index_text_view, null);
            index_text_view.setText(index);
            ll_quick_index.addView(index_text_view);
            index_text_view.setOnClickListener(this);
        }
    }

    /**
     * 解析json数据
     * @param jsonObj
     * @throws JSONException
     */
    public void parseJson(JSONObject jsonObj) throws JSONException {
        String data = jsonObj.getString("data");
        JSONObject jsonCity = new JSONObject(data);
        Iterator<String> iterator = jsonCity.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            quickIndexList.add(key);
            LogUtil.i(this, "key:" + key);
            String value = jsonCity.getString(key);
            List<CityInfoBean> citys = (List<CityInfoBean>) JsonUtil.parseJsonToList(value, new TypeToken<List<CityInfoBean>>() {
            }.getType());
            for(int i = 0;i<citys.size();i++){
                citys.get(i).group = key;
                indexPositionList.add(key);
            }
            list.addAll(citys);
            Collections.sort(list);
            Collections.sort(indexPositionList);
            Collections.sort(quickIndexList);
        }
    }

    /**
     * 快速索引条的点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v instanceof TextView){
            String index = ((TextView) v).getText().toString();
            int position = indexPositionList.indexOf(index);
            LogUtil.i(this,"索引:"+index);
            LogUtil.i(this,"位置:"+position+";城市组："+list.get(position).group);
            if(adapter!=null){
                lv_city_list.setSelection(position);
            }
        }
    }


    /**
     * list view 条目的点击事件处理
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.i(this,list.get(position).name);
        if(flag==Constant.TYPE_FRAGMENT_CITY_LIST_MORE){
            LogUtil.i(this,list.get(position).name+"---查看网点详情");
            Intent intent = new Intent(getActivity(), WebActivity.class);
            String url = String.format(Api.CityHtmlDetail,list.get(position).id);
            intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
            intent.putExtra(Constant.SERVICE_DETAIL_TITLE,"网点详情");
            startActivity(intent);
        }else if(flag==Constant.TYPE_FRAGMENT_CITY_LIST_HOT){
            LogUtil.i(this, list.get(position).name + "---返回地址");
            Intent intent = new Intent();
            intent.putExtra(SelectCityForAddressActivity.SelectCityName, list.get(position).name);
            intent.putExtra(SelectCityForAddressActivity.SelectCityID, list.get(position).id);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }
}
