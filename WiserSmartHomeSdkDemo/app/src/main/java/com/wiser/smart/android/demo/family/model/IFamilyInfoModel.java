package com.wiser.smart.android.demo.family.model;

import com.wiser.smart.home.sdk.callback.IWiserGetMemberListCallback;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;
import com.wiser.smart.sdk.api.IResultCallback;

public interface IFamilyInfoModel {

    void getHomeBean(long homeId, IWiserHomeResultCallback callback);

    void getMemberList(long homeId,IWiserGetMemberListCallback callback);

    void removeHome(long homeId, IResultCallback callback);

}
