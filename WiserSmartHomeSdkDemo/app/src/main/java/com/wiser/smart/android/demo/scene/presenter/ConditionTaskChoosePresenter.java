package com.wiser.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;

import com.wiser.smart.android.demo.scene.activity.DeviceChooseActivity;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.scene.event.ScenePageCloseEvent;
import com.wiser.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.sdk.WiserSdk;

import static com.wiser.smart.android.demo.scene.presenter.ScenePresenter.IS_CONDITION;


/**
 * create by nielev on 2019-10-29
 */
public class ConditionTaskChoosePresenter extends BasePresenter implements ScenePageCloseEvent {
    private Activity mAc;

    public ConditionTaskChoosePresenter(Activity activity){
        mAc = activity;
        WiserSdk.getEventBus().register(this);
    }

    public void selectDeviceTask(boolean isCondition){
        Intent intent = new Intent(mAc, DeviceChooseActivity.class);
        intent.putExtra(IS_CONDITION, isCondition);
        ActivityUtils.startActivity(mAc, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(ScenePageCloseModel model) {
        mAc.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WiserSdk.getEventBus().unregister(this);
    }
}
