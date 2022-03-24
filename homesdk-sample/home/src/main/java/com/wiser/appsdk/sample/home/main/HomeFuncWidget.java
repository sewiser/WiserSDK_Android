/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Wiser Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.wiser.appsdk.sample.home.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wiser.appsdk.sample.home.list.adapter.HomeListActivity;
import com.wiser.appsdk.sample.home.list.adapter.HomeListPageType;
import com.wiser.appsdk.sample.home.newHome.NewHomeActivity;
import com.wiser.appsdk.sample.resource.HomeModel;
import com.wiser.appsdk.sample.user.R;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.callback.IWiserHomeResultCallback;

/**
 * Home Management Widget
 *

 * @since 2/19/21 10:04 AM
 */
public class HomeFuncWidget implements View.OnClickListener {

    private TextView mTvNewHome, mTvCurrentHome, mTvCurrentHomeName, mTvHomeList;
    private Context mContext;

    public View render(Context context) {
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.home_view_func, null, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvNewHome = rootView.findViewById(R.id.tvNewHome);
        mTvCurrentHome = rootView.findViewById(R.id.tvCurrentHome);
        mTvCurrentHomeName = rootView.findViewById(R.id.tvCurrentHomeName);
        mTvHomeList = rootView.findViewById(R.id.tvHomeList);

        mTvNewHome.setOnClickListener(this);
        mTvCurrentHome.setOnClickListener(this);
        mTvHomeList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvCurrentHome) {// Switch Home
            Intent intent = new Intent(mContext, HomeListActivity.class);
            intent.putExtra("type", HomeListPageType.SWITCH);
            mContext.startActivity(intent);
        } else if (id == R.id.tvHomeList) {// Get Home List And Home Detail
            Intent intent = new Intent(mContext, HomeListActivity.class);
            intent.putExtra("type", HomeListPageType.LIST);
            mContext.startActivity(intent);
        } else if (id == R.id.tvNewHome) {// Create Home
            mContext.startActivity(new Intent(mContext, NewHomeActivity.class));
        }
    }


    public void refresh() {
        long currentHomeId = HomeModel.getCurrentHome(mContext);
        if (currentHomeId != 0L) {
            WiserHomeSdk.newHomeInstance(currentHomeId).getHomeDetail(new IWiserHomeResultCallback() {
                @Override
                public void onSuccess(HomeBean bean) {
                    mTvCurrentHomeName.setText(bean.getName());
                }

                @Override
                public void onError(String errorCode, String errorMsg) {

                }
            });

        }

    }
}
