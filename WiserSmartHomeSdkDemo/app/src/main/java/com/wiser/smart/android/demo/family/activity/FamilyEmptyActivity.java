package com.wiser.smart.android.demo.family.activity;

import android.content.Intent;
import android.os.Bundle;

import com.wiser.smart.android.demo.R;
import com.wiser.smart.android.demo.base.activity.BaseActivity;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.base.utils.LoginHelper;
import com.wiser.smart.android.user.api.ILogoutCallback;
import com.wiser.smart.home.sdk.WiserHomeSdk;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FamilyEmptyActivity extends BaseActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_empty);
        unbinder = ButterKnife.bind(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onBackPressed() {

    }

    @OnClick(R.id.activity_family_logout_btn)
    public void onLogoutClick(){
        WiserHomeSdk.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
        LoginHelper.reLogin(this, false);
    }


    @OnClick(R.id.activity_family_empty_btn)
    public void onFamilyEmptyClick(){
        Intent intent = new Intent(this, FamilyAddActivity.class);
        intent.putExtra(FamilyAddActivity.KEY_EMPTY_FAMILY,true);
        ActivityUtils.startActivity(this,intent,  ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM,
                false);
    }
}