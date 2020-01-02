package com.wiser.smart.android.demo.family.model;

import android.content.Context;

import com.wiser.smart.android.mvp.model.BaseModel;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.callback.IWiserGetMemberListCallback;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;
import com.wiser.smart.sdk.api.IResultCallback;

public class FamilyInfoModel extends BaseModel implements IFamilyInfoModel {

    public FamilyInfoModel(Context ctx) {
        super(ctx);
    }

    @Override
    public void getHomeBean(long homeId, IWiserHomeResultCallback callback) {
        WiserHomeSdk.newHomeInstance(homeId).getHomeDetail(callback);
    }

    @Override
    public void getMemberList(long homeId, IWiserGetMemberListCallback callback) {
        WiserHomeSdk.getMemberInstance().queryMemberList(homeId, callback);
    }

    @Override
    public void removeHome(long homeId, IResultCallback callback) {
        WiserHomeSdk.newHomeInstance(homeId).dismissHome(callback);
    }


    @Override
    public void onDestroy() {

    }
}
