package com.wiser.smart.android.demo.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wiser.smart.android.demo.R;
import com.wiser.smart.android.demo.base.activity.BaseActivity;
import com.wiser.smart.android.demo.base.activity.BrowserActivity;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;


/**
 * Created by ccx on 16/3/29.
 */
public class ECFailedActivity extends BaseActivity {

    public static final String FROM = "from";
    public static final int FROM_EC_ACTIVITY = 1;
    public static final int FROM_EC_BIND_ACTIVITY = 2;
    private static String TAG = "ECFailedActivity";
    private String mPassWord;
    private String mSSId;
    private int mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_failed);
        initToolbar();
        setTitle(R.string.choose_wifi);
        setDisplayHomeAsUpEnabled();
        mPassWord = getIntent().getStringExtra(ECActivity.CONFIG_PASSWORD);
        mSSId = getIntent().getStringExtra(ECActivity.CONFIG_SSID);
        mFrom = getIntent().getIntExtra(FROM, FROM_EC_BIND_ACTIVITY);
        if (mFrom == FROM_EC_ACTIVITY) {
            setViewGone(findViewById(R.id.tv_network_tip));
        } else {

        }
        findViewById(R.id.tv_bottom_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ECFailedActivity.this, ECBindActivity.class);
                intent.putExtra(ECActivity.CONFIG_PASSWORD, mPassWord);
                intent.putExtra(ECActivity.CONFIG_SSID, mSSId);
                intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
                ActivityUtils.startActivity(ECFailedActivity.this, intent, ActivityUtils.ANIMATE_FORWARD, true);
            }
        });
    }

}
