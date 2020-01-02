package com.wiser.smart.android.demo.family.activity;

import android.content.Context;

import com.wiser.smart.home.sdk.bean.HomeBean;

import java.util.List;

public interface IFamilyIndexView {

     Context getContext();

     void  setFamilyList(List<HomeBean> homeBeanList);

     void  showFamilyEmptyView();

}
