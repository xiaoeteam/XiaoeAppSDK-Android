package com.xiaoeshopsdk.sample.utils;

/**
 * Created by york on 2019-06-26.
 */
public class Const {
	public static final String SHOP_URL = constant(
			"https://app38itor341547.sdk.xiaoe-tech.com/",
			"https://134.175.39.17:9887/");

	public static final String APP_ID = constant(
			"app38itOR341547",
			"appdRx8JicQ9960");

	public static final String CLIENT_ID = constant(
			"KfeSEfFWTwTzfkE",
			"KfeSEfFWTwTzfkE");

	public static final String LOGIN_URL = SHOP_URL + "sdk_api/xe.account.login.test/1.0.0";
	private static final boolean PRODUCTION = true;

	private static String constant(String... candidates) {
		if (PRODUCTION) {
			return candidates[0];
		} else {
			return candidates[1];
		}
	}
}