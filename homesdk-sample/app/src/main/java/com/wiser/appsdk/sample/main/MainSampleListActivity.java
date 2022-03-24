/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Wiser Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.wiser.appsdk.sample.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.wiser.appsdk.sample.R;
import com.wiser.appsdk.sample.device.config.main.DeviceConfigFuncWidget;
import com.wiser.appsdk.sample.device.mgt.main.DeviceMgtFuncWidget;
import com.wiser.appsdk.sample.home.main.HomeFuncWidget;
import com.wiser.appsdk.sample.resource.HomeModel;
import com.wiser.appsdk.sample.user.info.UserInfoActivity;
import com.wiser.appsdk.sample.user.main.UserFuncActivity;
import com.wiser.smart.android.user.api.ILogoutCallback;
import com.wiser.smart.home.sdk.WiserHomeSdk;


/**
 * Sample Main List Page
 *
 */
public final class MainSampleListActivity extends AppCompatActivity {


    public HomeFuncWidget homeFuncWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_sample_list);

        findViewById(R.id.tvUserInfo).setOnClickListener(v -> {
            // User Info
            startActivity(new Intent(MainSampleListActivity.this, UserInfoActivity.class));

        });


        findViewById(R.id.tvLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WiserHomeSdk.getUserInstance().logout(new ILogoutCallback() {
                    @Override
                    public void onSuccess() {
                        // Clear cache
                        HomeModel.INSTANCE.clear(MainSampleListActivity.this);

                        // Navigate to User Func Navigation Page
                        Intent intent = new Intent(MainSampleListActivity.this, UserFuncActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                    }
                });
            }
        });

        LinearLayout llFunc = findViewById(R.id.llFunc);

        // Home Management
        homeFuncWidget = new HomeFuncWidget();
        llFunc.addView(homeFuncWidget.render(this));

        // Device Configuration Management
        DeviceConfigFuncWidget deviceConfigFucWidget = new DeviceConfigFuncWidget();
        llFunc.addView(deviceConfigFucWidget.render(this));

        // Device Management
        DeviceMgtFuncWidget deviceMgtFuncWidget = new DeviceMgtFuncWidget();
        llFunc.addView(deviceMgtFuncWidget.render(this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        homeFuncWidget.refresh();
    }
}