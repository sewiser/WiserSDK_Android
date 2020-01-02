package com.wiser.smart.android.demo.personal;


import com.wiser.smart.android.mvp.view.IView;

/**
 * Created by letian on 15/6/22.
 */
public interface IPersonalInfoView extends IView {

    void reNickName(String nickName);

    void onLogout();
}
