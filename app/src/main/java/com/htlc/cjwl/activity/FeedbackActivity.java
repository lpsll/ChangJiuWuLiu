package com.htlc.cjwl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.util.LogUtil;

/**
 * Created by sks on 2015/11/3.
 */
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private EditText et_feedback;
    private TextView tv_commit;
    private String mFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.feedback);

        et_feedback = (EditText) findViewById(R.id.et_feedback);
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
    }
}
