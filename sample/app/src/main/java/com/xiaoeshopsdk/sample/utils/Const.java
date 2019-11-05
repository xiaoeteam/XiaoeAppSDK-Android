package com.xiaoeshopsdk.sample.utils;

/**
 * Created by york on 2019-06-26.
 */
public class Const {
	public static final String SHOP_URL = constant(
			"https://appbq69bphk4153.sdk.xiaoe-tech.com/",
			"http://134.175.39.17:9887/");

	public static final String APP_ID = constant(
			"appbQ69BphK4153",
			"appdRx8JicQ9960");

	public static final String CLIENT_ID = constant(
			"bm3TJyxMQJG5",
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