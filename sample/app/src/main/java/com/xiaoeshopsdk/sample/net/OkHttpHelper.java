package com.xiaoeshopsdk.sample.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHelper {
    public static final int TOKEN_MISSING = 401;    //token¶ªÊ§
    public static final int TOKEN_ERROR = 402;      //token´íÎó
    public static final int TOKEN_EXPIRE = 403;     //token¹ýÆÚ

    private static OkHttpClient okHttpClient;
    private Handler handler;
    private Gson gson;

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, BaseCallBack callBack) {
        get(url, null, callBack);
    }

    public void get(String url, Map<String, Object> params, BaseCallBack callBack) {
        Request request = buildGetRequest(url, params);
        doRequest(request, callBack);
    }

    public void post(String url, Map<String, Object> params, BaseCallBack callBack) {
        post(url, params, null, callBack);
    }

    public void post(String url, Map<String, Object> params, Map<String, String> headers, BaseCallBack callBack) {
        Request request = buildPostRequest(url, params, headers);
        doRequest(request, callBack);
    }

    public Response doGet(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return doRequest(buildGetRequest(url, params, headers));
    }

    public Response doPost(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return doRequest(buildPostRequest(url, params, headers));
    }

    public Response doPost(String url, String body, Map<String, String> headers) throws IOException {
        return doRequest(buildPostRequest(url, body, headers));
    }

    private Response doRequest(final Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    public void doRequest(final Request request, final BaseCallBack callBack) {
        callBack.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackFailed(callBack, call.request(), e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callbackResponse(callBack, response);
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    Log.d("okhttpresult11", "the result = " + resultStr);
                    if (callBack.mType == String.class) {
                        callbackSuccess(callBack, response, resultStr);
                    } else {
                        try {
                            Object object = gson.fromJson(resultStr, callBack.mType);
                            callbackSuccess(callBack, response, object);
                        } catch (JsonParseException e) {
                            callbackError(callBack, response, response.code(), e);
                        }
                    }
                } else if (response.code() == TOKEN_MISSING || response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE) {
                    callbackTokenError(callBack, response);
                } else {
                    callbackError(callBack, response, response.code(), null);
                }
            }
        });
    }

    private void callbackResponse(final BaseCallBack callBack, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onResponse(response);
            }
        });
    }

    private void callbackFailed(final BaseCallBack callBack, final Request request, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(request, e);
            }
        });
    }

    private void callbackSuccess(final BaseCallBack callBack, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response, object);
            }
        });
    }

    private void callbackTokenError(final BaseCallBack callBack, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onTokenError(response, response.code());
            }
        });
    }

    private void callbackError(final BaseCallBack callBack, final Response response, final int code, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(response, code, e);
            }
        });
    }

    private Request buildPostRequest(String url, Map<String, Object> params) {
        return buildPostRequest(url, params, null);
    }

    private Request buildPostRequest(String url, Map<String, Object> params, Map<String, String> headers) {
        return buildRequest(url, params, headers, HttpMethodType.POST);
    }

    private Request buildPostRequest(String url, String body, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        RequestBody requestBody = buildRequestBody(headers, body);
        builder.post(requestBody);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }
        return builder.build();
    }

    private Request buildGetRequest(String url, Map<String, Object> params) {
        return buildGetRequest(url, params, null);
    }

    private Request buildGetRequest(String url, Map<String, Object> params, Map<String, String> headers) {
        return buildRequest(url, params, headers, HttpMethodType.GET);
    }

    private Request buildRequest(String url, Map<String, Object> params, Map<String, String> headers, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }
        return builder.build();
    }

    private String buildUrlParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" : entry.getValue().toString());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }
        return url;
    }

    private RequestBody buildFormData(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }
        }
        return builder.build();
    }

    private RequestBody buildRequestBody(Map<String, String> headers, String body) {
        String contentType = null;
        if (headers != null) {
            contentType = headers.get("Content-Type");
        }
        if (contentType == null) {
            contentType = "application/json;charset=UTF-8";
        }
        return RequestBody.create(MediaType.parse(contentType), body);
    }

    private enum HttpMethodType {
        GET, POST
    }
}