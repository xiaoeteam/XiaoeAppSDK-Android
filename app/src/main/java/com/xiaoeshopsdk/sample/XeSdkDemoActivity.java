package com.xiaoeshopsdk.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.xiaoe.shop.webcore.XEToken;
import com.xiaoe.shop.webcore.XiaoEWeb;
import com.xiaoe.shop.webcore.bridge.JsBridgeListener;
import com.xiaoe.shop.webcore.bridge.JsCallbackResponse;
import com.xiaoe.shop.webcore.bridge.JsInteractType;
import com.xiaoeshopsdk.sample.net.LoginEntity;
import com.xiaoeshopsdk.sample.net.OkHttpHelper;
import com.xiaoeshopsdk.sample.net.SimpleCallBack;
import com.xiaoeshopsdk.sample.utils.Const;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Request;
import okhttp3.Response;

public class XeSdkDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog mLoginDlg;
    private Button mConfirmBtn, mCancelBtn;
    private EditText mUserNameEdit;

    private XiaoEWeb xiaoEWeb;
    private OkHttpHelper mOkHttpHelper;

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

        mOkHttpHelper = OkHttpHelper.getInstance();
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xiaoEWeb.webLifeCycle().onResume();
    }

    private void initView() {
        View loginLayout = LayoutInflater.from(XeSdkDemoActivity.this).inflate(R.layout.login_dialog_layout, null);
        mConfirmBtn = loginLayout.findViewById(R.id.confirm_login_btn);
        mCancelBtn = loginLayout.findViewById(R.id.cancel_login_btn);
        mUserNameEdit = loginLayout.findViewById(R.id.user_name_edit);
        mLoginDlg = new AlertDialog.Builder(XeSdkDemoActivity.this).setView(loginLayout).create();
        mLoginDlg.setCancelable(false);

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
                        //如果登录失败，通过XiaoEWeb.syncNot()清除登录态（注意用户需要实现自己的SDK的登录）
                        if (mLoginDlg != null && !mLoginDlg.isShowing())
                            mLoginDlg.show();
                        break;

                    case JsInteractType.SHARE_ACTION:
                        //H5分享请求回调，通过 response.getResponseData() 获取分享的数据，这里三方 APP 自行分享登录方法
                        Toast.makeText(XeSdkDemoActivity.this, response.getResponseData(), Toast.LENGTH_SHORT).show();
                        break;

                    case JsInteractType.TITLE_RECEIVE:
                        Log.d("TITLE_RECEIVE22", "can go back = " + xiaoEWeb.canGoBack());
                        //H5标题回调，通过 response.getResponseData() 获取标题
                        break;
                }
            }
        });
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_login_btn:
                if (!TextUtils.isEmpty(mUserNameEdit.getText())) {
                    doLogin(mUserNameEdit.getText().toString());
                } else {
                    Toast.makeText(XeSdkDemoActivity.this, R.string.user_name_empty_tip, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel_login_btn:
                if (mLoginDlg != null && mLoginDlg.isShowing()) {
                    mLoginDlg.dismiss();
                    xiaoEWeb.loginCancel();
                }
                break;
        }
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
                finish();
                return true;
            case R.id.share:
                xiaoEWeb.share();
                return true;
            case R.id.logout:
                xiaoEWeb.syncNot();
                return true;
            case R.id.fresh:
                if (xiaoEWeb != null)
                    xiaoEWeb.getUrlLoader().reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (xiaoEWeb.handlerKeyEvent(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    private void doLogin(String userName){
        Map<String, Object> params = new HashMap<>(4);
        params.put("user_id", userName);
        params.put("app_user_id", userName);
        params.put("secret_key", Const.SECRET_KEY);
        params.put("sdk_app_id", Const.CLIENT_ID);
        params.put("app_id", Const.APP_ID);
        mOkHttpHelper.post(Const.SDK_LOGIN_URL, params, new SimpleCallBack<LoginEntity>(XeSdkDemoActivity.this) {
            @Override
            public void onFailure(Request request, IOException e) {
                mLoginDlg.dismiss();
                Toast.makeText(XeSdkDemoActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Response response, LoginEntity loginEntity) {
                mLoginDlg.dismiss();
                if (loginEntity != null && loginEntity.getCode() == 0 && loginEntity.getData() != null) {
                    Toast.makeText(XeSdkDemoActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    XEToken token = new XEToken(loginEntity.getData().getToken_key(), loginEntity.getData().getToken_value());
                    xiaoEWeb.sync(token);
                } else {
                    Toast.makeText(XeSdkDemoActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mLoginDlg.dismiss();
                Toast.makeText(XeSdkDemoActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}