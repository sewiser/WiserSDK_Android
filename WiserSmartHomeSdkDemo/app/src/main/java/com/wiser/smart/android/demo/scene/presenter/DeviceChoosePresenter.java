package com.wiser.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;

import com.wiser.smart.android.base.utils.PreferencesUtil;
import com.wiser.smart.android.demo.scene.activity.OperateorListActivity;
import com.wiser.smart.android.demo.base.app.Constant;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.base.utils.ToastUtil;
import com.wiser.smart.android.demo.scene.event.ScenePageCloseEvent;
import com.wiser.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.wiser.smart.android.demo.scene.view.IDeviceChooseView;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.callback.IWiserResultCallback;
import com.wiser.smart.sdk.WiserSdk;
import com.wiser.smart.sdk.bean.DeviceBean;

import java.util.List;


/**
 * create by nielev on 2019-10-29
 */
public class DeviceChoosePresenter extends BasePresenter implements ScenePageCloseEvent {
    private Activity mAc;
    private IDeviceChooseView mView;

    public static final String DEV_ID = "dev_id";
    private boolean isCondition;

    public DeviceChoosePresenter(Activity activity, IDeviceChooseView view){
        mAc = activity;
        mView = view;
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
        WiserSdk.getEventBus().register(this);
    }
    public void getDevList(){
        isCondition = mAc.getIntent().getBooleanExtra(ScenePresenter.IS_CONDITION, false);
        if(isCondition){
            WiserHomeSdk.getSceneManagerInstance().getConditionDevList(Constant.HOME_ID, new IWiserResultCallback<List<DeviceBean>>() {
                @Override
                public void onSuccess(List<DeviceBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showDevices(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    ToastUtil.shortToast(mAc, errorMessage);
                }
            });
        } else {
            WiserHomeSdk.getSceneManagerInstance().getTaskDevList(Constant.HOME_ID, new IWiserResultCallback<List<DeviceBean>>() {
                @Override
                public void onSuccess(List<DeviceBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showDevices(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    ToastUtil.shortToast(mAc, errorMessage);
                }
            });
        }

    }

    public void getDeviceOperatorList(DeviceBean deviceBean) {
        Intent intent = new Intent(mAc, OperateorListActivity.class);
        intent.putExtra(ScenePresenter.IS_CONDITION, isCondition);
        intent.putExtra(DEV_ID, deviceBean.getDevId());
        ActivityUtils.startActivity(mAc, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WiserSdk.getEventBus().unregister(this);
    }

    @Override
    public void onEvent(ScenePageCloseModel model) {
        mAc.finish();
    }
}
