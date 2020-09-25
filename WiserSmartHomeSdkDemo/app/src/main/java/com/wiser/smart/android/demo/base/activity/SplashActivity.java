package com.wiser.smart.android.demo.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.wiser.smart.android.common.utils.L;
import com.wiser.smart.android.common.utils.WiserUtil;
import com.wiser.smart.android.demo.base.utils.ActivityUtils;
import com.wiser.smart.android.demo.base.utils.DialogUtil;
import com.wiser.smart.android.demo.base.utils.LoginHelper;
import com.wiser.smart.android.demo.login.activity.LoginActivity;
import com.wiser.smart.home.sdk.WiserHomeSdk;


/**
 * Created by letian on 16/7/19.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("splash", "wiserTime: " + WiserUtil.formatDate(System.currentTimeMillis(), "yyyy-mm-dd hh:mm:ss"));
        if (isInitAppkey()) {
            gotoLogin();
        } else {
            showTipDialog();
        }
    }

    public void gotoLogin() {
        if (WiserHomeSdk.getUserInstance().isLogin()) {//已登录，跳主页
            LoginHelper.afterLogin();
            ActivityUtils.gotoHomeActivity(this);
        } else {
            ActivityUtils.gotoActivity(this, LoginActivity.class, ActivityUtils.ANIMATE_FORWARD, true);
        }
    }


    private void showTipDialog() {
        DialogUtil.simpleConfirmDialog(this, "appkey or appsecret is empty. \nPlease check your configuration", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    private boolean isInitAppkey() {
        String appkey = getInfo("WISER_SMART_APPKEY", this);
        String appSecret = getInfo("WISER_SMART_SECRET", this);
        if (TextUtils.equals("null", appkey) || TextUtils.equals("null", appSecret)) return false;
        return !TextUtils.isEmpty(appkey) && !TextUtils.isEmpty(appSecret);
    }

    public static String getInfo(String infoName, Context context) {
        ApplicationInfo e;
        try {
            e = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            return e.metaData.getString(infoName);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
