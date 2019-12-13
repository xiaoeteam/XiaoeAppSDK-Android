package com.xiaoeshopsdk.sample;

import android.app.Application;
import com.xiaoe.shop.webcore.XiaoEWeb;
import com.xiaoeshopsdk.sample.utils.Const;

public class SdkDemoApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        //为了使用的稳定性，建议此处指定使用X5内核的WebView
        XiaoEWeb.init(this, Const.APP_ID, Const.CLIENT_ID, XiaoEWeb.WebViewType.X5);
    }
}