package com.xiaoeshopsdk.sample;

import android.app.Application;
import com.xiaoe.shop.webcore.XiaoEWeb;
import com.xiaoeshopsdk.sample.utils.Const;

public class SdkDemoApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        XiaoEWeb.init(this, Const.APP_ID, Const.CLIENT_ID, XiaoEWeb.WebViewType.X5);
    }
}