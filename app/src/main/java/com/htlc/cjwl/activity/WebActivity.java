package com.htlc.cjwl.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;

/**
 * Created by sks on 2015/11/4.
 * 进行网页数据显示的Activity
 */
public class WebActivity  extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;//Activity 标题上的 返回键
    private TextView tv_activity_title;//Activity的标题
    private WebView wb_web_view;//显示网页 的 web view控件

    private String service_detail_id;//请求网页的id（网页的url）
    private String service_detail_title;//网页的标题  需要在开启Activity传递的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        service_detail_id = getIntent().getStringExtra(Constant.SERVICE_DETAIL_ID);
        service_detail_title = getIntent().getStringExtra(Constant.SERVICE_DETAIL_TITLE);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        wb_web_view = (WebView) findViewById(R.id.wb_web_view);

        //设置网页标题
        tv_activity_title.setText(service_detail_title);

        initWebView();
        initData();
        iv_back.setOnClickListener(this);


    }

    /**
     * 加载网页数据
     */
    private void initData() {
        wb_web_view.loadUrl(service_detail_id);
    }

    /**
     * 初始化，配置web view
     */
    private void initWebView() {
        //运行js代码
        wb_web_view.getSettings().setJavaScriptEnabled(true);
        //先不加载图片
        if(Build.VERSION.SDK_INT >= 19) {
            wb_web_view.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wb_web_view.getSettings().setLoadsImagesAutomatically(false);
        }
        //给webview设置客户端
        wb_web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
        //不显示滚动条
        wb_web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
        }
    }
}
