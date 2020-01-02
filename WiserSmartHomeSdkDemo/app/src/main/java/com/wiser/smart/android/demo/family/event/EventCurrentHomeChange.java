package com.wiser.smart.android.demo.family.event;

import com.wiser.smart.home.sdk.bean.HomeBean;

public class EventCurrentHomeChange {

    private HomeBean homeBean;

    public EventCurrentHomeChange(HomeBean homeBean) {
        this.homeBean = homeBean;
    }

    public HomeBean getHomeBean() {
        return homeBean;
    }

    public void setHomeBean(HomeBean homeBean) {
        this.homeBean = homeBean;
    }
}
