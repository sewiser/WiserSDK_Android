package com.wiser.smart.android.demo.base.presenter;

import android.app.Activity;
import android.content.Context;

import com.wiser.smart.android.demo.base.activity.PersonalInfoActivity;
import com.wiser.smart.android.demo.personal.PersonalInfoEvent;
import com.wiser.smart.android.demo.personal.PersonalInfoEventModel;
import com.wiser.smart.android.demo.personal.IPersonalCenterModel;
import com.wiser.smart.android.demo.personal.PersonalCenterModel;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.personal.IPersonalCenterView;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.sdk.WiserSdk;


/**
 * Created by letian on 15/6/1.
 */
public class PersonalCenterFragmentPresenter extends BasePresenter implements PersonalInfoEvent {
    private static final String TAG = "PersonalCenterFragmentPresenter";
    private final Context mContext;
    private final IPersonalCenterView mView;
    private final IPersonalCenterModel mModel;


    public PersonalCenterFragmentPresenter(Context context, IPersonalCenterView view) {
        mContext = context;
        mView = view;
        mModel = new PersonalCenterModel(context, mHandler);
        WiserSdk.getEventBus().register(this);
    }


    public void setPersonalInfo() {
        mView.setNickName(mModel.getNickName());
        mView.setUserName(mModel.getUserName());
    }

    public void gotoPersonalInfoActivity() {
        ActivityUtils.gotoActivity((Activity) mContext, PersonalInfoActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(PersonalInfoEventModel event) {
        mView.setNickName(mModel.getNickName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
        WiserSdk.getEventBus().unregister(this);
    }


}
