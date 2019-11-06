## 小鹅通App内嵌SDK（Android端）
为了满足移动应用搭建知识商城的需求，小鹅通将知识商品交易系统开放，移动开发者通过一个 SDK 便可以在 App 内集成小鹅通提供的整个交易服务，享受完善的基础知识商品能力、营销玩法，更有小鹅通强劲的技术及服务作保障，实现低成本、高效率、强融合的移动商城方案，快速获得 App 流量的商业化变现。

小鹅通 App内嵌SDK服务目前提供的是 H5 版 SDK，将小鹅通提供的 HTML5 页面嵌入到 App 中，基于此提供资产合并、订单管理、支付营销能力开放等 App 应用特色功能，更拥有媲美原生页面的性能。该方案接入极速、功能完善、性能稳定。

## 引入

```
allprojects {
    repositories {
        maven {
            url  "https://dl.bintray.com/xiaoeteam/xiaoeSDK"
        }
    }
}
```

在子项目build.gradle的dependencies中根据需求引入依赖:
```
compile 'com.xiaoe.shop.webcore:1.0.1'
```
## 文档
[接入文档](https://github.com/xiaoeteam/XiaoeAppSDK-Android/wiki "接入文档")

