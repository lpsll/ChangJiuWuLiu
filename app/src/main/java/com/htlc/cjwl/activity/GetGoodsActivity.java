package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.htlc.cjwl.R;


/**
 * Created by luochuan on 15/11/5.
 * 留做扩展使用
 */
public class GetGoodsActivity extends Activity {

    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_goods);
    }

}
