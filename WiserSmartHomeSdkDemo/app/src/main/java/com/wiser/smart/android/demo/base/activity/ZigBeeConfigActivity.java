package com.wiser.smart.android.demo.base.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.wiser.smart.android.common.utils.L;
import com.wiser.smart.android.demo.R;
import com.wiser.smart.android.demo.base.app.Constant;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.builder.WiserGwActivatorBuilder;
import com.wiser.smart.home.sdk.builder.WiserGwSubDevActivatorBuilder;
import com.wiser.smart.home.sdk.callback.IWiserGetHomeListCallback;
import com.wiser.smart.sdk.api.IWiserActivator;
import com.wiser.smart.sdk.api.IWiserActivatorGetToken;
import com.wiser.smart.sdk.api.IWiserSmartActivatorListener;
import com.wiser.smart.sdk.bean.DeviceBean;

import java.util.List;

public class ZigBeeConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ZigBeeConfigActivity";
    private IWiserActivator iWiserActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zig_bee_config);

        findViewById(R.id.btn_start_config_gw).setOnClickListener(this);
        findViewById(R.id.btn_start_config_sub_dev).setOnClickListener(this);
        findViewById(R.id.btn_local_scene).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_config_gw:
                configGW();
                break;
            case R.id.btn_start_config_sub_dev:
                configSubDev();
                break;
            case R.id.btn_local_scene:

                break;
        }

    }

    private void configSubDev() {
        List<DeviceBean> devList = WiserHomeSdk.getDataInstance().getHomeDeviceList(Constant.HOME_ID);
        for (DeviceBean deviceBean : devList) {
            if (deviceBean.isZigBeeWifi())
                WiserHomeSdk.getActivatorInstance().newWiserGwActivator().newSubDevActivator(new WiserGwSubDevActivatorBuilder()
                        .setListener(new IWiserSmartActivatorListener() {
                            @Override
                            public void onError(String errorCode, String errorMsg) {

                            }

                            @Override
                            public void onActiveSuccess(DeviceBean devResp) {
                                L.d(TAG, " devResp: " + devResp.getDevId());
                            }

                            @Override
                            public void onStep(String step, Object data) {

                            }
                        })
                        .setDevId(deviceBean.getDevId())).start();

        }
    }

    private void configGW() {
        WiserHomeSdk.getHomeManagerInstance().queryHomeList(new IWiserGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                WiserHomeSdk.getActivatorInstance().getActivatorToken(homeBeans.get(0).getHomeId(), new IWiserActivatorGetToken() {
                    @Override
                    public void onSuccess(final String token) {
                        startConfigGW(token);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMsg) {

                    }
                });
            }

            @Override
            public void onError(String errorCode, String error) {

            }
        });


    }

    private void startConfigGW(String token) {
        WiserHomeSdk.getActivatorInstance().newGwActivator(new WiserGwActivatorBuilder()
                .setToken(token)
                .setContext(this)
                .setListener(new IWiserSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {
                        L.d(TAG, " devResp: " + devResp.getDevId());
                    }

                    @Override
                    public void onStep(String step, Object data) {

                    }
                }).setToken(token)).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
