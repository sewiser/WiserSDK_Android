package com.wiser.smart.android.demo.family.model;

import android.content.Context;

import com.wiser.smart.android.demo.family.FamilyManager;
import com.wiser.smart.android.mvp.model.BaseModel;
import com.wiser.smart.home.sdk.callback.IWiserGetHomeListCallback;

public class FamilyIndexModel extends BaseModel implements IFamilyIndexModel {

    public static final String TAG = FamilyIndexModel.class.getSimpleName();

    public FamilyIndexModel(Context ctx) {
        super(ctx);
    }


    @Override
    public void queryHomeList(IWiserGetHomeListCallback callback) {
        FamilyManager.getInstance().getHomeList(callback);
    }

    @Override
    public void onDestroy() {

    }

}
