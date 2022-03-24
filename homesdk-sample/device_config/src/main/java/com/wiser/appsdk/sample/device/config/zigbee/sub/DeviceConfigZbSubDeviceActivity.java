/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Wiser Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NO
 */
package com.wiser.appsdk.sample.device.config.zigbee.sub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.wiser.appsdk.sample.device.config.R;
import com.wiser.appsdk.sample.device.config.util.sp.SpUtils;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.builder.WiserGwSubDevActivatorBuilder;
import com.wiser.smart.sdk.api.IWiserActivator;
import com.wiser.smart.sdk.api.IWiserSmartActivatorListener;
import com.wiser.smart.sdk.bean.DeviceBean;

/**
 * Device Configuration ZbSubDevice Sample
 *

 * @since 2021/3/3 10:59 AM
 */
public class DeviceConfigZbSubDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CURRENT_GATEWAY_ID = "current_gateway_id";
    public static final String CURRENT_GATEWAY_NAME = "current_gateway_name";
    private MaterialToolbar topAppBar;
    private TextView tv_current_zb_gateway;
    private TextView tvCurrentGateway;
    private Button btnSearch;
    private CircularProgressIndicator cpiLoading;

    int REQUEST_CODE = 1003;

    private String currentGatewayName;
    private String currentGatewayId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_config_zb_sub_device_activity);
        initView();

        currentGatewayName = SpUtils.getInstance().getString(CURRENT_GATEWAY_NAME, "0");
        currentGatewayId = SpUtils.getInstance().getString(CURRENT_GATEWAY_ID, "0");
    }

    private void initView() {

        /*// init gatewayName and gatewayId
        currentGatewayName = "";
        currentGatewayId = "";*/


        topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topAppBar.setTitle(R.string.device_config_zb_sub_device_title);

        // choose zigBee gateway
        tv_current_zb_gateway = findViewById(R.id.tv_current_zb_gateway);
        tv_current_zb_gateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(DeviceConfigZbSubDeviceActivity.this, DeviceConfigChooseZbGatewayActivity.class), 1003);
            }
        });
        tvCurrentGateway = findViewById(R.id.tv_current_gateway_name);
        btnSearch = findViewById(R.id.btnSearch);
        cpiLoading = findViewById(R.id.cpiLoading);

        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            subDeviceConfiguration();
        }
    }

    // Sub-device Configuration
    private void subDeviceConfiguration() {
        if (TextUtils.isEmpty(tvCurrentGateway.getText())) {
            Toast.makeText(DeviceConfigZbSubDeviceActivity.this,
                    "Please select gateway first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        setPbViewVisible(true);

        WiserGwSubDevActivatorBuilder gwSubDevActivatorBuilder = new WiserGwSubDevActivatorBuilder()
                .setDevId(currentGatewayId)
                .setTimeOut(100)
                .setListener(new IWiserSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {

                        setPbViewVisible(false);
                        Toast.makeText(
                                DeviceConfigZbSubDeviceActivity.this,
                                "Active Error" + errorMsg,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {

                        setPbViewVisible(false);
                        Toast.makeText(
                                DeviceConfigZbSubDeviceActivity.this,
                                "Active Success",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }

                    @Override
                    public void onStep(String step, Object data) {

                    }
                });
        IWiserActivator iWiserActivator = WiserHomeSdk.getActivatorInstance().newGwSubDevActivator(gwSubDevActivatorBuilder);
        iWiserActivator.start();
    }

    private void setPbViewVisible(boolean b) {
        cpiLoading.setVisibility(b ? View.VISIBLE : View.GONE);
        btnSearch.setEnabled(!b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            tvCurrentGateway.setText(currentGatewayName);
        }
    }
}
