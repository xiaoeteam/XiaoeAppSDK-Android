package com.xiaoeshopsdk.sample;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.xiaoe.shop.webcore.XiaoEWeb;
import com.xiaoe.shop.webcore.bridge.JsBridgeListener;
import com.xiaoe.shop.webcore.bridge.JsCallbackResponse;
import com.xiaoe.shop.webcore.bridge.JsInteractType;
import com.xiaoeshopsdk.sample.utils.Const;

public class XeSdkDemoActivity extends AppCompatActivity{

    private XiaoEWeb xiaoEWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xiaoEWeb.webLifeCycle().onResume();
    }

    private void initView() {
        RelativeLayout webLayout = findViewById(R.id.web_layout);
        xiaoEWeb = XiaoEWeb.with(this)
                .setWebParent(webLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultUI()
                .useDefaultTopIndicator(getResources().getColor(R.color.colorPrimaryDark))
                .buildWeb()
                .loadUrl(Const.SHOP_URL);
    }

    private void initEvent() {
        xiaoEWeb.setJsBridgeListener(new JsBridgeListener() {
            @Override
            public void onJsInteract(int actionType, JsCallbackResponse response) {
                switch (actionType) {
                    case JsInteractType.LOGIN_ACTION:
                        //H5登录态请求 这里三方 APP 应该调用登陆接口，获取到token_key, token_value
                        //如果登录成功，通过XiaoEWeb.sync(XEToken)方法同步登录态到H5页面
                        //如果登录失败，通过XiaoEWeb.syncNot()清除登录态
                        Toast.makeText(XeSdkDemoActivity.this, "SDK未获取登录态，请先登录！", Toast.LENGTH_SHORT).show();
                        break;

                    case JsInteractType.SHARE_ACTION:
                        //H5分享请求回调，通过 response.getResponseData() 获取分享的数据，这里三方 APP 自行分享登录方法
                        Toast.makeText(XeSdkDemoActivity.this, response.getResponseData(), Toast.LENGTH_SHORT).show();
                        break;

                    case JsInteractType.LOGOUT_ACTION:
                        xiaoEWeb.syncNot();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tools, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                //触发分享
                xiaoEWeb.share();
                return true;
            case R.id.logout:
                //清除登录态
                xiaoEWeb.syncNot();
                return true;
            case R.id.fresh:
                //刷新页面
                if (xiaoEWeb != null)
                    xiaoEWeb.getUrlLoader().reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //WebView的返回处理
        if (xiaoEWeb.handlerKeyEvent(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        xiaoEWeb.webLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xiaoEWeb.webLifeCycle().onDestroy();
    }
}