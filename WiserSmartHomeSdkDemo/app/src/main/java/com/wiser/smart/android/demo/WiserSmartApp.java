package com.wiser.smart.android.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.wiser.smart.android.common.utils.L;
import com.wiser.smart.android.demo.login.activity.LoginActivity;
import com.wiser.smart.home.sdk.WiserHomeSdk;
import com.wiser.smart.sdk.WiserSdk;
import com.wiser.smart.sdk.api.INeedLoginListener;


public class WiserSmartApp extends Application {

    private static final String TAG = "WiserSmartApp";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        L.d(TAG, "onCreate " + getProcessName(this));
        WiserSdk.init(this);
        WiserSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, LoginActivity.class);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private static Context context;

    public static Context getAppContext() {
        return context;
    }


}
