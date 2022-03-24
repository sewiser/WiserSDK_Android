package com.wiser.appsdk.sample.device.mgt.group;

import com.wiser.smart.android.base.ApiParams;
import com.wiser.smart.android.network.Business;

public class GroupBusiness extends Business {


    public void getSigMeshGroupLocalId(String meshId, Business.ResultListener<String> listener) {
        ApiParams apiParams = new ApiParams("tuya.m.device.ble.mesh.local.id.alloc", "2.0");
        apiParams.setSessionRequire(true);
        apiParams.putPostData("meshId", meshId);
        apiParams.putPostData("type", 1);
        asyncRequest(apiParams, String.class, listener);
    }
}
