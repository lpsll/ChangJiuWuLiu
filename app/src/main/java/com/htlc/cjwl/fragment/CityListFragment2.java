package com.htlc.cjwl.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.CitysListAdapter;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.Citys;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luochuan on 15/11/9.
 */
public class CityListFragment2 extends Fragment implements View.OnClickListener{


    private ArrayList<String> quickIndexList = new ArrayList<String>();
    private ArrayList<String> indexPositionList = new ArrayList<String>();
    private ArrayList<CityInfoBean> list = new ArrayList<CityInfoBean>();
    private CitysListAdapter adapter;
    private Citys cityList;
    private JSONObject listCity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_city_list, null);


        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
