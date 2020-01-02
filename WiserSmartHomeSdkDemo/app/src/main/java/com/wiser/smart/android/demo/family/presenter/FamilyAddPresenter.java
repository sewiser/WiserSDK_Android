package com.wiser.smart.android.demo.family.presenter;

import com.wiser.smart.android.demo.base.utils.CollectionUtils;
import com.wiser.smart.android.demo.family.activity.IFamilyAddView;
import com.wiser.smart.android.demo.family.model.FamilyAddModel;
import com.wiser.smart.android.demo.family.model.IFamilyAddModel;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;

import java.util.List;

public class FamilyAddPresenter extends BasePresenter {


    private IFamilyAddModel mFamilyAddModel;

    private IFamilyAddView mFamilyAddView;

    public FamilyAddPresenter(IFamilyAddView view) {
        super(view.getContext());
        this.mFamilyAddView = view;
        this.mFamilyAddModel = new FamilyAddModel();
    }


    public void addFamily(String homeName, List<String> roomList) {
        if (CollectionUtils.isEmpty(roomList)) {
            return;
        }
        mFamilyAddModel.createHome(homeName, roomList, new IWiserHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                if (null == mFamilyAddView) {
                    return;
                }
                mFamilyAddView.doSaveSuccess();
            }

            @Override
            public void onError(String s, String s1) {
                if (null == mFamilyAddView) {
                    return;
                }
                mFamilyAddView.doSaveFailed();
            }
        });
    }


}
