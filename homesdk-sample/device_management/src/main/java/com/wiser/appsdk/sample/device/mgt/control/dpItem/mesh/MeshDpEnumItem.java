/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Wiser Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NO
 */

package com.wiser.appsdk.sample.device.mgt.control.dpItem.mesh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wiser.appsdk.sample.device.mgt.R;
import com.wiser.smart.android.blemesh.api.IWiserBlueMeshDevice;
import com.wiser.smart.android.device.bean.EnumSchemaBean;
import com.wiser.smart.android.device.bean.SchemaBean;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.utils.SchemaMapper;
import com.wiser.smart.sdk.api.IResultCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.collections.CollectionsKt;

/**
 * Data point(DP) Enum type item
 *

 * @since 2021/2/21 3:06 PM
 * <p>
 * The current class is used to issue dp (Boolean) directives to a mesh device or group.
 * </p>
 */
public class MeshDpEnumItem extends FrameLayout {

    private final String TAG = "MeshDpBooleanItem";
    public MeshDpEnumItem(Context context,
                          AttributeSet attrs,
                          int defStyle,
                          SchemaBean schemaBean,
                          String value,
                          String meshId,
                          boolean isGroup,
                          String localOrNodeId,
                          String pcc) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.device_mgt_item_dp_enum, this);

        TextView tvDpName = findViewById(R.id.tvDpName);
        tvDpName.setText(schemaBean.name);

        Button btnDp = findViewById(R.id.btnDp);
        btnDp.setText(value);

        if (schemaBean.mode.contains("w")) {
            // Data can be issued by the cloud.
            IWiserBlueMeshDevice mWiserSigMeshDevice= WiserHomeSdk.newSigMeshDeviceInstance(meshId);

            ListPopupWindow listPopupWindow = new ListPopupWindow(context, null, R.attr.listPopupWindowStyle);
            listPopupWindow.setAnchorView(btnDp);

            EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.property);
            Set set = enumSchemaBean.range;
            List items = CollectionsKt.toList(set);
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.device_mgt_item_dp_enum_popup_item, items);
            listPopupWindow.setAdapter(adapter);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {

                Map map = new HashMap();

                map.put(schemaBean.id, items.get(position));
                if (isGroup) {
                    mWiserSigMeshDevice.multicastDps(localOrNodeId, pcc, JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Log.d(TAG, "send dps error:" + error);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "send dps success");
                            btnDp.setText((CharSequence) items.get(position));
                        }
                    });
                }else{
                    mWiserSigMeshDevice.publishDps(localOrNodeId, pcc, JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Log.d(TAG, "send dps error:" + error);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "send dps success");
                            btnDp.setText((CharSequence) items.get(position));
                        }
                    });
                }

                listPopupWindow.dismiss();


            });
            btnDp.setOnClickListener(v -> {
                listPopupWindow.show();
            });
        }
    }
}
