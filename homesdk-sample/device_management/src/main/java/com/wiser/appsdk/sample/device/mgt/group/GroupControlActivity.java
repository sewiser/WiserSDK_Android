package com.wiser.appsdk.sample.device.mgt.group;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wiser.appsdk.sample.device.mgt.R;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpBooleanItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpCharTypeItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpEnumItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpIntegerItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpRawTypeItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.normal.DpBooleanItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.normal.DpCharTypeItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.normal.DpEnumItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.DpFaultItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.normal.DpIntegerItem;
import com.wiser.appsdk.sample.device.mgt.control.dpItem.normal.DpRawTypeItem;
import com.wiser.appsdk.sample.resource.HomeModel;
import com.wiser.smart.android.device.bean.BitmapSchemaBean;
import com.wiser.smart.android.device.bean.BoolSchemaBean;
import com.wiser.smart.android.device.bean.EnumSchemaBean;
import com.wiser.smart.android.device.bean.SchemaBean;
import com.wiser.smart.android.device.bean.StringSchemaBean;
import com.wiser.smart.android.device.bean.ValueSchemaBean;
import com.wiser.smart.android.device.enums.DataTypeEnum;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;
import com.wiser.smart.sdk.bean.DeviceBean;
import com.wiser.smart.sdk.bean.GroupBean;
import com.wiser.smart.sdk.bean.SigMeshBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GroupControlActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity_control);
        initView();
        initSchemaList();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initSchemaList(){
        long groupId = getIntent().getLongExtra("groupId",-1);

        if (groupId == -1){
            finish();
            return;
        }
        List<SigMeshBean> meshList = WiserHomeSdk.getSigMeshInstance().getSigMeshList();
        if (meshList == null || meshList.isEmpty()){
            finish();
            return;
        }
        String meshID = null;
        for (SigMeshBean sigMeshBean : meshList){
            if (sigMeshBean != null){
                meshID = sigMeshBean.getMeshId();
                if (!TextUtils.isEmpty(meshID)){
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(meshID)) {
            finish();
            return;
        }
        long homeId = HomeModel.getCurrentHome(this);
        String finalMeshID = meshID;
        WiserHomeSdk.newHomeInstance(homeId).getHomeDetail(new IWiserHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                if (bean != null){
                    List<GroupBean> groupBeanList = bean.getGroupList();
                    if (groupBeanList != null && !groupBeanList.isEmpty()){
                        for (GroupBean groupBean : groupBeanList){
                            if (groupBean != null && groupId == groupBean.getId()){
                                prepareGroupController(finalMeshID,groupBean);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });

    }
    private void prepareGroupController(String meshID,GroupBean groupBean){
        if (TextUtils.isEmpty(meshID) || groupBean == null){
            return;
        }
        String pcc = groupBean.getCategory();
        String localId = groupBean.getLocalId();
        if (TextUtils.isEmpty(pcc) || TextUtils.isEmpty(localId)){
            return;
        }
        LinearLayout llDp = findViewById(R.id.llDp);
        String deviceId = getIntent().getStringExtra("deviceId");
        if (TextUtils.isEmpty(deviceId)){
            return;
        }
        DeviceBean deviceBean = WiserHomeSdk.getDataInstance().getDeviceBean(deviceId);
        if (deviceBean == null){
            return;
        }
        TextView tvGroupName = findViewById(R.id.tv_group_name);
        tvGroupName.setText(groupBean.getName());
        Map<String, SchemaBean> map = WiserHomeSdk.getDataInstance().getSchema(deviceId);
        if (map == null || map.size() <= 0){
            return;
        }
        Collection<SchemaBean> schemaBeans = map.values();
        for (SchemaBean bean : schemaBeans) {
            Object value = deviceBean.getDps().get(bean.getId());
            if (value == null){
                continue;
            }
            if (bean.type.equals(DataTypeEnum.OBJ.getType())) {
                // obj
                switch (bean.getSchemaType()) {

                    case BoolSchemaBean.type:
                        MeshDpBooleanItem dpBooleanItem = new MeshDpBooleanItem(
                                this,null,0,
                                bean,
                                (Boolean) value,
                                meshID,true,localId,pcc);
                        llDp.addView(dpBooleanItem);
                        break;

                    case EnumSchemaBean.type:
                        MeshDpEnumItem dpEnumItem = new MeshDpEnumItem(
                                this,null,0,
                                bean,
                                value.toString(),
                                meshID,true,localId,pcc);
                        llDp.addView(dpEnumItem);
                        break;

                    case StringSchemaBean.type:
                        MeshDpCharTypeItem dpCharTypeItem = new MeshDpCharTypeItem(
                                this,null,0,
                                bean,
                                (String) value,
                                meshID,true,localId,pcc);
                        llDp.addView(dpCharTypeItem);
                        break;

                    case ValueSchemaBean.type:
                        MeshDpIntegerItem dpIntegerItem = new MeshDpIntegerItem(
                                this,null,0,
                                bean,
                                (int) value,
                                meshID,true,localId,pcc);
                        llDp.addView(dpIntegerItem);
                        break;

                    case BitmapSchemaBean.type:
                        DpFaultItem dpFaultItem = new DpFaultItem(
                                this,
                                bean,
                                value.toString());
                        llDp.addView(dpFaultItem);
                }

            } else if (bean.type.equals(DataTypeEnum.RAW.getType())) {
                // raw | file
                if (value == null) {
                    value = "null";
                }
                MeshDpRawTypeItem dpRawTypeItem = new MeshDpRawTypeItem(
                        this,null,0,
                        bean,
                        value.toString(),
                        meshID,true,localId,pcc);
                llDp.addView(dpRawTypeItem);

            }
        }
    }
}