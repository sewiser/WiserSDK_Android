package com.wiser.smart.android.demo.family.activity;

import android.content.Context;

import com.wiser.smart.home.sdk.bean.HomeBean;
import com.wiser.smart.home.sdk.bean.MemberBean;

import java.util.List;

public interface IFamilyInfoView {

    Context getContext();

    long getHomeId();

    void setHomeData(HomeBean homeBean);

    void setMemberData(List<MemberBean> memberList);

    void doRemoveView(boolean isSuccess);

    void showToast(int res);
}
