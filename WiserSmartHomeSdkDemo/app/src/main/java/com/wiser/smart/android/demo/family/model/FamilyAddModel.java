package com.wiser.smart.android.demo.family.model;

import com.wiser.smart.android.demo.family.FamilyManager;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;

import java.util.List;

public class FamilyAddModel implements IFamilyAddModel {

    public static final String TAG = FamilyAddModel.class.getSimpleName();

    @Override
    public void createHome(String homeName,
                           List<String> roomList,
                           IWiserHomeResultCallback callback) {
        FamilyManager.getInstance().createHome(homeName, roomList, callback);
    }

}
