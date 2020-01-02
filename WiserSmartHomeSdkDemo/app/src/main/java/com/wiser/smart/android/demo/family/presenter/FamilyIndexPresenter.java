package com.wiser.smart.android.demo.family.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wiser.smart.android.demo.base.utils.CollectionUtils;
import com.wiser.smart.android.demo.family.activity.IFamilyIndexView;
import com.wiser.smart.android.demo.family.model.FamilyIndexModel;
import com.wiser.smart.android.demo.family.model.IFamilyIndexModel;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.callback.IWiserGetHomeListCallback;

import java.util.List;

public class FamilyIndexPresenter extends BasePresenter {

    public static final String TAG = FamilyIndexPresenter.class.getSimpleName();

    private IFamilyIndexModel mFamilyIndexModel;

    private IFamilyIndexView iFamilyIndexView;


    public FamilyIndexPresenter(final IFamilyIndexView iFamilyIndexView) {
        super();
        this.iFamilyIndexView = iFamilyIndexView;
        this.mFamilyIndexModel = new FamilyIndexModel(iFamilyIndexView.getContext());

        getFamilyList();
    }


    public void getFamilyList() {
        mFamilyIndexModel.queryHomeList(new IWiserGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                Log.i(TAG, "queryHomeList onSuccess: " + JSON.toJSONString(list));
                if (null == iFamilyIndexView.getContext()) {
                    return;
                }
                if (CollectionUtils.isEmpty(list)) {
                    iFamilyIndexView.showFamilyEmptyView();
                } else {
                    iFamilyIndexView.setFamilyList(list);
                }
            }

            @Override
            public void onError(String s, String s1) {
                Log.e(TAG, "queryHomeList onError: errorCode=" + s);
                // TODO: 2018/12/17  
                
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
