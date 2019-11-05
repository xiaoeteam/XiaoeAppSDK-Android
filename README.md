小鹅通店铺SDK Android接入说明
#### 一、前置条件
1、注册使用小鹅店铺，取得店铺appID
2、购买小鹅店铺sdk接入服务，取得sdkAppId
#### 二、引入SDK
Android SDK暂时只提供aar的方式进行下载，eclipse开发等待后续升级
1、SDK文件说明：
SDK内包含aar文件、sdk使用demo、sdk接入文档、sdk更新说明
#### 三、接入指引
###### 1.引入方式
将”xiaoe_shop_sdk_release_vx.x.x.aar”文件拷贝进待接入sdk的moudle下的“libs”目录下，并加入gradle依赖。若“libs”文件夹不存在，需新建该文件夹
![](http://doc.xiaoeknow.com/server/../Public/Uploads/2019-11-01/5dbbe3a29c422.png)

###### 2.权限声明

AndroidManifest.xml里加入权限声明如下：
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.CAMERA"/>
```
AndroidManifest.xml里开启硬件加速和配置屏幕旋转，必须在加载小鹅店铺的webview所在activity上配置，如下：
```xml
<activity android:name=".XXXActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
	android:hardwareAccelerated="true"/>
```
###### 3.初始化SDK
在自定义Application中对SDK进行初始化，如下：
```java
public class XXXplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
		//初始化SDK
        XiaoEWeb.init(this, "app_id", "sdk_id", XiaoEWeb.WebViewType.X5);
    }
}
```
参数说明：
this：Context上下文参数
app_id：店铺Id
sdk_id：申请嵌入SDK获取的Id
type：WebView内核类型（原生：WebViewType.Android；腾讯X5内核：WebViewType.X5；建议使用腾讯X5内核）

###### 4.加载Web页面
SDK采用动态添加WebView的方式去加载网页，无需在布局文件中添加WebView，只需要在调用时指定WebView的布局容器即可。具体调用方式如下：
```java
XiaoEWeb.with(this)
        .setWebParent(webLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        .useDefaultUI()
        .useDefaultTopIndicator(getResources().getColor(R.color.colorPrimaryDark))
        .buildWeb()
        .loadUrl(Const.SHOP_URL);
```
setWebParent：设置webview的父容器和LayoutParams；
useCustomUI：设置自定义错误页和加载页的布局Id，若不需要自定义可直接使用useDefaultUI()方法；
useDefaultTopIndicator：设置顶部进度条的样式，包括颜色和高度等
buildWeb：构建WebView
loadUrl：加载网页


还可以通过下面的调用方式完成一些其他设置：
```java
XiaoEWeb.with(this)
  .setWebParent()
  .useCustomUI()
  .useCustomTopIndicator()
  .setWebSetting()
  .setUrlLoader()
  .setWebUIController()
  .setWebViewClient()
  .setWebChromeClient()
  .buildWeb()
  .loadUrl()
```

###### 5.注册店铺页面回调
sdk针对小鹅店铺提供如下的页面回调事件:

```java
public class JsInteractType {
    public static final int SHARE_ACTION = 1;   //分享事件
    public static final int LOGIN_ACTION = 2;   //登录事件
    public static final int LOGOUT_ACTION = 3;  //登出事件
}
```
注册方式：通过注册JsBridgeListener实现
```java
public interface JsBridgeListener{
    void onJsInteract(int actionType, JsCallbackResponse response);
}
```
在监听器内通过actionType区分不同的回调事件，具体实现方式参考demo实现。

###### 6.登录授权同步
小鹅的店铺，必须需要token鉴权才能进入店铺商品详情页等业务操作或个人信息页面，（获取token的方式见API文档）获取到token后需调用下面方法进行同步：
```java
public void sync(XEToken xeToken){}
```
XEToken说明：（key与value必须业务后台从小鹅sdk后台获取到的key、value一致）
```java
public class XEToken{
    private String tokenKey;
    private String tokenValue;
}
```

将token设置进sdk，在适当的时机需清空token信息（如：退出登录）
```java
public void syncNot(){}
```
###### 7.WebView生命周期管理
为了避免内存泄漏，需要在activity或fragment的对应生命周期函数内调用webview对应的生命周期函数：
```java
XiaoEWeb.webLifeCycle().onResume();
XiaoEWeb.webLifeCycle().onPause();
XiaoEWebwebLifeCycle().onDestroy();
```
###### 8.页面预加载
为了提供对某些页面的加载速度，sdk提供了预加载实现方案（暂时只支持一级页面的预加载）
```java
XiaoEWeb.preloadingUrl(context,url);
```

###### 9.功能性方法
```java
clearWebViewCache()：清除webview加载缓存
loginCancel()：退出登录
share()：获取分享内容（暂只支持分享店铺首页）
```
