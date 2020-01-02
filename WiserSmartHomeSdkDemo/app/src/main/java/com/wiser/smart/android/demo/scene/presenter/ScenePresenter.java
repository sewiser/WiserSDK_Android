package com.wiser.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.wiser.smart.android.demo.R;
import com.wiser.smart.android.demo.base.app.Constant;
import com.wiser.smart.android.demo.base.presenter.SceneListPresenter;
import com.wiser.smart.android.demo.base.utils.ToastUtil;
import com.wiser.smart.android.demo.scene.activity.ConditionTaskChooseActivity;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.scene.event.SceneUpdateCondtionEvent;
import com.wiser.smart.android.demo.scene.event.SceneUpdateTaskEvent;
import com.wiser.smart.android.demo.scene.event.model.SceneUpdateConditionModel;
import com.wiser.smart.android.demo.scene.event.model.SceneUpdateTaskModel;
import com.wiser.smart.android.demo.scene.view.ISceneView;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.bean.scene.SceneBean;
import com.wiser.smart.home.sdk.bean.scene.SceneCondition;
import com.wiser.smart.home.sdk.bean.scene.SceneTask;
import com.wiser.smart.home.sdk.callback.IWiserResultCallback;
import com.wiser.smart.sdk.WiserSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * create by nielev on 2019-10-29
 */
public class ScenePresenter extends BasePresenter implements SceneUpdateCondtionEvent, SceneUpdateTaskEvent {
    public static final String IS_CONDITION = "is_condition";
    private Activity mAc;
    private final int mSmartType;
    private final boolean isEdit;
    private ISceneView mView;
    private SceneBean mSceneBean;
    public ScenePresenter(Activity activity, ISceneView view){
        mAc = activity;
        mView = view;
        mSmartType = mAc.getIntent().getIntExtra(SceneListPresenter.SMART_TYPE, 0);
        isEdit = mAc.getIntent().getBooleanExtra(SceneListPresenter.SMART_IS_EDIT, false);
        if(mSmartType == SceneListPresenter.SMART_TYPE_SCENE){
            mView.showSceneView();
        } else {
            mView.showAutoView();
        }
        if(isEdit){

        } else {
            mSceneBean = new SceneBean();
        }
        WiserSdk.getEventBus().register(this);
    }

    public void addCondition(){
        gotoCondtionTaskChoose(true);
    }

    public void addTask(){
        gotoCondtionTaskChoose(false);
    }

    private void gotoCondtionTaskChoose(boolean isCondtion){
        Intent intent = new Intent(mAc, ConditionTaskChooseActivity.class);

        intent.putExtra(IS_CONDITION, isCondtion);

        ActivityUtils.startActivity(mAc, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(SceneUpdateConditionModel model) {
        List<SceneCondition> conditions = mSceneBean.getConditions();
        if(null == conditions){
            conditions = new ArrayList<>();
        }
        conditions.add(model.getmSceneConditon());
        mSceneBean.setConditions(conditions);
        mView.updateConditions(conditions);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WiserSdk.getEventBus().unregister(this);
    }

    @Override
    public void onEvent(SceneUpdateTaskModel model) {
        List<SceneTask> tasks = mSceneBean.getActions();
        if(null == tasks){
            tasks = new ArrayList<>();
        }
        tasks.add(model.getSceneTask());
        mSceneBean.setActions(tasks);
        mView.updateTasks(tasks);
    }

    public void addBg() {
        WiserHomeSdk.getSceneManagerInstance().getSceneBgs(new IWiserResultCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                if(null != result && !result.isEmpty()){
                    Random random = new Random();
                    String bg = result.get(random.nextInt(result.size()));
                    mSceneBean.setBackground(bg);
                    mView.showBg(bg);
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    public void save() {
        String sceneName = mView.getSceneName();
        if(TextUtils.isEmpty(sceneName)){
            ToastUtil.shortToast(mAc, R.string.ty_scene_name_is_not_empty);
            return;
        }
        if(TextUtils.isEmpty(mSceneBean.getBackground())){
            ToastUtil.shortToast(mAc, R.string.ty_scene_bg_is_not_empty);
            return;
        }
        if(mSmartType == SceneListPresenter.SMART_TYPE_AUTOMATION){
            if(null == mSceneBean.getConditions() || mSceneBean.getConditions().isEmpty()){
                ToastUtil.shortToast(mAc, R.string.ty_scene_condition_is_not_empty);
                return;
            }
        }
        if(null == mSceneBean.getActions() || mSceneBean.getActions().isEmpty()){
            ToastUtil.shortToast(mAc, R.string.ty_scene_action_is_not_empty);
            return;
        }
        WiserHomeSdk.getSceneManagerInstance().createScene(Constant.HOME_ID, sceneName, mSceneBean.getBackground(), mSceneBean.getConditions(), mSceneBean.getActions(), 0, new IWiserResultCallback<SceneBean>() {
            @Override
            public void onSuccess(SceneBean result) {
                ToastUtil.shortToast(mAc, R.string.save_success);
                mAc.finish();
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                ToastUtil.shortToast(mAc, errorMessage);
            }
        });
    }
}
