package com.wiser.smart.android.demo.scene.view;

import com.wiser.smart.sdk.bean.DeviceBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public interface IDeviceChooseView {
    void showDevices(List<DeviceBean> deviceBeans);

    void showEmpty();
}
