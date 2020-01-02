package com.wiser.smart.android.demo.personal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wiser.smart.android.demo.R;


/**
 * Created by Kunyang.Lee on 2017/9/15.
 */

public class FeedbackActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        findViewById(R.id.bt_get_feedback_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_feedback_list:
                break;
        }
    }

}
