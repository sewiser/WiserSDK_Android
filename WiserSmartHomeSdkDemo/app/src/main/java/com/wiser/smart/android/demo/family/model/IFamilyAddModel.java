package com.wiser.smart.android.demo.family.model;

import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;

import java.util.List;

public interface IFamilyAddModel {
    void createHome(String homeName, List<String> roomList,IWiserHomeResultCallback callback);
}
