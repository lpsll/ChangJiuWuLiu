package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import util.LogUtil;
import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 * 订单列表---订单评价Activity
 */
public class EvaluationActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String OrderID = "OrderID";
    public  String orderId;//订单id
    private ImageView iv_back;//返回按钮
    private TextView tv_activity_title;//标题
    private EditText et_feedback;//评论编辑框
    private TextView tv_commit;//提交评论按钮
    private String mFeedback;
    private RatingBar ratingBar;//评分按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        orderId = getIntent().getStringExtra(OrderID);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.evaluation);

        et_feedback = (EditText) findViewById(R.id.et_feedback);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tv_commit = (TextView) findViewById(R.id.tv_commit);

        iv_back.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_commit:
                LogUtil.i(this,"提交信息！！");
                commit();
                break;
        }
    }

    /**
     * 提交操作
     */
    private void commit() {
        String feedbackStr = et_feedback.getText().toString().trim();
        float grade = ratingBar.getRating();
        App.appAction.evaluationOrder(orderId, feedbackStr, grade + "", new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "评价成功！");
                finish();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
