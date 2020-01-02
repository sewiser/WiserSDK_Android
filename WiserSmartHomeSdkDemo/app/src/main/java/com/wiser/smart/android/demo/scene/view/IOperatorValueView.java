package com.wiser.smart.android.demo.scene.view;

import com.wiser.smart.android.demo.scene.bean.OperatorBean;
import com.wiser.smart.android.device.bean.ValueSchemaBean;
import com.wiser.smart.home.sdk.bean.scene.dev.TaskListBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public interface IOperatorValueView {
    void showValueView(boolean isCondition, TaskListBean taskListBean);

    void showEnumOrBooleanValueView(List<OperatorBean> operatorBeans);

    String getOperator();

    int getValueTypeChoose(ValueSchemaBean valueSchemaBean);
}
