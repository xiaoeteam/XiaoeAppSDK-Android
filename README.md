<p>
<a href="https://github.com/xiaoeteam"><img alt="小鹅通logo" width="130px" src="https://www.xiaoe-tech.com/images/pageBase/logo_blue@2x.png" alt="xiaoe">
</a>
</p>

## 简述

通过小鹅通 APP 内嵌 SDK 可以在 App 内快速集成小鹅通提供的整个交易服务，享受完善的基础知识商品能力、营销玩法，更有小鹅通强劲的技术及服务作保障，实现低成本、高效率、强融合的移动商城方案，快速获得 App 流量的商业化变现。

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
implementation 'com.xiaoe.shop:webcore:1.0.8'
```
## 文档
[接入文档](https://github.com/xiaoeteam/XiaoeAppSDK-Android/wiki "接入文档")

