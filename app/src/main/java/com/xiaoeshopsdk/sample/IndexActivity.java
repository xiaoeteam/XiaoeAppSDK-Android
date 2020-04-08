package com.xiaoeshopsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaoe.shop.webcore.core.XiaoEWeb;

public class IndexActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);

		TextView logoutTv = findViewById(R.id.user_logout_tv);
		logoutTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//在三方 App 内发生用户切换或用户退出的时, 为了避免出现客户信息混乱, 请务必执行如下代码登出小鹅通用户角色.
				XiaoEWeb.userLogout(IndexActivity.this.getApplicationContext());
			}
		});

		TextView testShopTv = findViewById(R.id.test_shop_tv);
		testShopTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(IndexActivity.this, XeSdkDemoActivity.class));
			}
		});
	}
}