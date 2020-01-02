package com.wiser.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.wiser.smart.android.demo.R;
import com.wiser.smart.android.demo.base.utils.ToastUtil;
import com.wiser.smart.android.demo.scene.bean.OperatorBean;
import com.wiser.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.wiser.smart.android.demo.scene.event.model.SceneUpdateConditionModel;
import com.wiser.smart.android.demo.scene.event.model.SceneUpdateTaskModel;
import com.wiser.smart.android.demo.scene.view.IOperatorValueView;
import com.wiser.smart.android.mvp.presenter.BasePresenter;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.bean.scene.SceneCondition;
import com.wiser.smart.home.sdk.bean.scene.SceneTask;
import com.wiser.smart.home.sdk.bean.scene.condition.property.BoolProperty;
import com.wiser.smart.home.sdk.bean.scene.condition.property.EnumProperty;
import com.wiser.smart.home.sdk.bean.scene.condition.property.ValueProperty;
import com.wiser.smart.home.sdk.bean.scene.condition.rule.BoolRule;
import com.wiser.smart.home.sdk.bean.scene.condition.rule.EnumRule;
import com.wiser.smart.home.sdk.bean.scene.condition.rule.Rule;
import com.wiser.smart.home.sdk.bean.scene.condition.rule.ValueRule;
import com.wiser.smart.home.sdk.bean.scene.dev.TaskListBean;
import com.wiser.smart.sdk.WiserSdk;
import com.wiser.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wiser.smart.android.demo.scene.event.model.ScenePageCloseModel.DEFAULT_CLOSE;

/**
 * create by nielev on 2019-10-29
 */
public class OperatorValuePresenter extends BasePresenter {
    private Activity mAc;
    private IOperatorValueView mView;
    private TaskListBean mTaskListBean;
    private final boolean isCondition;
    private final String mDevId;
    private OperatorBean mOperatorBean;
    public OperatorValuePresenter(Activity ac, IOperatorValueView view){
        mAc = ac;
        mView = view;
        Bundle bundle = mAc.getIntent().getBundleExtra("Bundle");
        mTaskListBean = (TaskListBean) bundle.getSerializable(OperatorListPresenter.TASK_LIST_BEAN);
        isCondition = bundle.getBoolean(ScenePresenter.IS_CONDITION, false);
        mDevId = bundle.getString(DeviceChoosePresenter.DEV_ID);
        if (null != mTaskListBean) {
            String type = mTaskListBean.getType();

            if(TextUtils.equals(type, ValueProperty.type)){
                mView.showValueView(isCondition, mTaskListBean);
            } else {
                HashMap<Object, String> tasks = mTaskListBean.getTasks();
                List<OperatorBean> operatorBeans = new ArrayList<>();
                if(null != tasks && !tasks.isEmpty()){
                    for (Map.Entry<Object, String> entry : tasks.entrySet()){
                        operatorBeans.add(new OperatorBean(entry.getKey(),entry.getValue()));
                    }
                }
                mView.showEnumOrBooleanValueView(operatorBeans);
            }
        }
    }

    public void saveOperator() {
        if(isCondition){
            saveCondition();
        } else {
            saveTask();
        }
    }

    private void saveTask() {
        if(TextUtils.equals(mTaskListBean.getType(),ValueProperty.type)){
            HashMap dpMap = new HashMap();
            int valueTypeChoose = mView.getValueTypeChoose(mTaskListBean.getValueSchemaBean());
            dpMap.put(mTaskListBean.getDpId(), valueTypeChoose);
            SceneTask dpTask = WiserHomeSdk.getSceneManagerInstance().createDpTask(mDevId, dpMap);
            HashMap<String, List<String>> actionDisplayNew = new HashMap<>();
            List<String> displays = new ArrayList<>();
            displays.add(mTaskListBean.getName());
            displays.add(valueTypeChoose+"");
            actionDisplayNew.put(mTaskListBean.getDpId()+"", displays);
            dpTask.setActionDisplayNew(actionDisplayNew);
            WiserSdk.getEventBus().post(new SceneUpdateTaskModel(dpTask));
            WiserSdk.getEventBus().post(new ScenePageCloseModel(DEFAULT_CLOSE));
            mAc.finish();
        } else {
            if(null == mOperatorBean){
                ToastUtil.shortToast(mAc, R.string.ty_scene_condition_is_not_empty);
                return;
            }
            HashMap dpMap = new HashMap();
            dpMap.put(mTaskListBean.getDpId(), mOperatorBean.getKey());
            SceneTask dpTask = WiserHomeSdk.getSceneManagerInstance().createDpTask(mDevId, dpMap);
            HashMap<String, List<String>> actionDisplayNew = new HashMap<>();
            List<String> displays = new ArrayList<>();
            displays.add(mTaskListBean.getName());
            displays.add(mOperatorBean.getValue());
            actionDisplayNew.put(mTaskListBean.getDpId()+"", displays);
            dpTask.setActionDisplayNew(actionDisplayNew);
            WiserSdk.getEventBus().post(new SceneUpdateTaskModel(dpTask));
            WiserSdk.getEventBus().post(new ScenePageCloseModel(DEFAULT_CLOSE));
            mAc.finish();
        }
    }

    private void saveCondition() {
        if(TextUtils.equals(mTaskListBean.getType(),ValueProperty.type)){
            String operator = mView.getOperator();
            int valueTypeChoose = mView.getValueTypeChoose(mTaskListBean.getValueSchemaBean());
            ValueRule valueRule = ValueRule.newInstance("dp" + mTaskListBean.getDpId(), operator, valueTypeChoose);
            DeviceBean deviceBean = WiserHomeSdk.getDataInstance().getDeviceBean(mDevId);
            if(null != deviceBean){
                SceneCondition devCondition = SceneCondition.createDevCondition(deviceBean, mTaskListBean.getDpId() + "", valueRule);
                devCondition.setIconUrl(deviceBean.getIconUrl());
                devCondition.setExprDisplay(mTaskListBean.getName()+operator+valueTypeChoose);
                WiserSdk.getEventBus().post(new SceneUpdateConditionModel(devCondition));
                WiserSdk.getEventBus().post(new ScenePageCloseModel(DEFAULT_CLOSE));
                mAc.finish();
            }else {
                ToastUtil.shortToast(mAc, R.string.ty_scene_device_is_not_exsit);
            }
        } else {
            if(null == mOperatorBean){
                ToastUtil.shortToast(mAc, R.string.ty_scene_condition_is_not_empty);
                return;
            }
            Rule rule = null;
            if(TextUtils.equals(mTaskListBean.getType(), BoolProperty.type)){
                rule = BoolRule.newInstance("dp" + mTaskListBean.getDpId(), (Boolean) mOperatorBean.getKey());
            } else if(TextUtils.equals(mTaskListBean.getType(), EnumProperty.type)){
                rule = EnumRule.newInstance("dp" + mTaskListBean.getDpId(), (String) mOperatorBean.getKey());
            }
            if(null == rule){
                ToastUtil.shortToast(mAc, R.string.ty_scene_operator_not_support);
                return;
            }
            DeviceBean deviceBean = WiserHomeSdk.getDataInstance().getDeviceBean(mDevId);
            if(null != deviceBean){
                SceneCondition devCondition = SceneCondition.createDevCondition(deviceBean, mTaskListBean.getDpId() + "", rule);
                devCondition.setIconUrl(deviceBean.getIconUrl());
                devCondition.setExprDisplay(mTaskListBean.getName()+"="+mOperatorBean.getValue());
                WiserSdk.getEventBus().post(new SceneUpdateConditionModel(devCondition));
                WiserSdk.getEventBus().post(new ScenePageCloseModel(DEFAULT_CLOSE));
                mAc.finish();
            }else {
                ToastUtil.shortToast(mAc, R.string.ty_scene_device_is_not_exsit);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void selectItem(OperatorBean bean) {
        mOperatorBean = bean;
    }
}
