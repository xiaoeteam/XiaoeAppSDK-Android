package com.xiaoeshopsdk.sample.utils;

/**
 * Created by york on 2019-06-26.
 */
public class Const {

	//三方 App 用户购买小鹅店铺sdk获取的店铺Url
	public static final String SHOP_URL = "https://app38itOR341547.sdk.xiaoe-tech.com/";
	//三方 App 用户注册使用小鹅店铺，取得店铺appID
	public static final String APP_ID = "app38itOR341547";
	//三方 App 用户购买小鹅店铺sdk接入服务，取得sdkAppId
	public static final String CLIENT_ID = "883pzzGyzynE72G";

	//下面两个接口不建议三方 App 用户直接在自己的app端进行调用，而是三方 App 用户后台写一个sdk登录的接口提供给自己的app使用，
	//当app请求后台的这个接口，由三方 App 用户的后台调用下面两个接口（我司的SDK登录接口），三方 App 用户的后台调用成功，把token返回给app
	public static final String GET_TOKEN_URL = "http://api.xiaoe-tech.com/token";
	public static final String LOGIN_URL = "http://api.xiaoe-tech.com/xe.sdk.account.login/1.0.0";
}