package com.xiaoeshopsdk.sample.net;

import android.content.Context;

import okhttp3.Request;
import okhttp3.Response;

public abstract class SimpleCallBack<T> extends BaseCallBack<T>
{
    protected Context mContext;
    public SimpleCallBack(Context context)
    {
        mContext = context;
    }

    @Override
    public void onRequestBefore(Request request){}

    @Override
    public void onResponse(Response response){}

    @Override
    public void onTokenError(Response response, int code){}
}
